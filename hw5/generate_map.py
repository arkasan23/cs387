import argparse
import random
from collections import deque
from pathlib import Path

WIDTH = 64
HEIGHT = 32
CA_STEPS = 5
MAX_ATTEMPTS = 200

GRASS = "."
TREE = "t"
WATER = "w"

TOWNHALL_W = 4
TOWNHALL_H = 4
MINE_W = 3
MINE_H = 3


def mirror_x(x, obj_width=1):
    return WIDTH - x - obj_width


TH1 = (6, 6)
TH2 = (mirror_x(TH1[0], TOWNHALL_W), TH1[1])

P1 = (5, 8)
P2 = (mirror_x(P1[0]), P1[1])

GOLD1 = (1, 5)
GOLD2 = (mirror_x(GOLD1[0], MINE_W), GOLD1[1])

MID_GOLD1 = (22, 24)
MID_GOLD2 = (mirror_x(MID_GOLD1[0], MINE_W), MID_GOLD1[1])


def initial_grid(rng):
    grid = [[GRASS for _ in range(WIDTH)] for _ in range(HEIGHT)]

    # only make half then mirror it
    for y in range(HEIGHT):
        for x in range(WIDTH // 2):
            r = rng.random()

            if r < 0.53:
                value = GRASS
            elif r < 0.84:
                value = TREE
            else:
                value = WATER

            grid[y][x] = value
            grid[y][WIDTH - 1 - x] = value

    return grid


def moore_counts(grid, x, y):
    blocked = 0
    trees = 0
    water = 0

    for dy in (-1, 0, 1):
        for dx in (-1, 0, 1):
            if dx == 0 and dy == 0:
                continue

            nx = x + dx
            ny = y + dy

            if nx < 0 or ny < 0 or nx >= WIDTH or ny >= HEIGHT:
                blocked += 1
                trees += 1
                continue

            value = grid[ny][nx]

            if value != GRASS:
                blocked += 1

            if value == TREE:
                trees += 1
            elif value == WATER:
                water += 1

    return blocked, trees, water


def ca_step(grid):
    next_grid = [[GRASS for _ in range(WIDTH)] for _ in range(HEIGHT)]

    for y in range(HEIGHT):
        for x in range(WIDTH):
            blocked, trees, water = moore_counts(grid, x, y)
            current = grid[y][x]

            if blocked >= 5:
                if water >= 4 or (current == WATER and water >= 3):
                    next_grid[y][x] = WATER
                else:
                    next_grid[y][x] = TREE

            elif blocked <= 3:
                next_grid[y][x] = GRASS

            else:
                next_grid[y][x] = current

    return next_grid


def set_rect(grid, x, y, w, h, value=GRASS):
    for yy in range(max(0, y), min(HEIGHT, y + h)):
        for xx in range(max(0, x), min(WIDTH, x + w)):
            grid[yy][xx] = value


def clear_gameplay_zones(grid):
    # open area for player 1
    set_rect(grid, 4, 3, 10, 12)

    # same area mirrored for player 2
    set_rect(grid, mirror_x(4, 10), 3, 10, 12)

    # clear around buildings and gold
    for x, y, w, h in (
        (*TH1, TOWNHALL_W, TOWNHALL_H),
        (*TH2, TOWNHALL_W, TOWNHALL_H),
        (*GOLD1, MINE_W, MINE_H),
        (*GOLD2, MINE_W, MINE_H),
        (*MID_GOLD1, MINE_W, MINE_H),
        (*MID_GOLD2, MINE_W, MINE_H),
    ):
        set_rect(grid, x - 1, y - 1, w + 2, h + 2)

    # clear peasant locations
    set_rect(grid, P1[0], P1[1], 1, 1)
    set_rect(grid, P2[0], P2[1], 1, 1)

    # make sure both players have some trees nearby
    for y in range(11, 15):
        for x in range(2, 4):
            if not (
                GOLD1[0] <= x < GOLD1[0] + MINE_W and GOLD1[1] <= y < GOLD1[1] + MINE_H
            ):
                grid[y][x] = TREE
                grid[y][WIDTH - 1 - x] = TREE


def is_passable(grid, x, y):
    return 0 <= x < WIDTH and 0 <= y < HEIGHT and grid[y][x] == GRASS


def target_ring(x, y, w, h):
    targets = set()

    for xx in range(x - 1, x + w + 1):
        targets.add((xx, y - 1))
        targets.add((xx, y + h))

    for yy in range(y, y + h):
        targets.add((x - 1, yy))
        targets.add((x + w, yy))

    return {(x0, y0) for x0, y0 in targets if 0 <= x0 < WIDTH and 0 <= y0 < HEIGHT}


def reachable(grid, start, targets):
    if not is_passable(grid, *start):
        return False

    valid_targets = {p for p in targets if is_passable(grid, *p)}

    if not valid_targets:
        return False

    queue = deque([start])
    seen = {start}

    while queue:
        x, y = queue.popleft()

        if (x, y) in valid_targets:
            return True

        for dx, dy in (
            (1, 0),
            (-1, 0),
            (0, 1),
            (0, -1),
        ):
            nx = x + dx
            ny = y + dy

            if (nx, ny) not in seen and is_passable(grid, nx, ny):
                seen.add((nx, ny))
                queue.append((nx, ny))

    return False


def has_build_space(grid, origin, size=5, radius=9):
    ox, oy = origin

    for y in range(max(1, oy - radius), min(HEIGHT - size, oy + radius) + 1):
        for x in range(max(1, ox - radius), min(WIDTH - size, ox + radius) + 1):
            good = True

            for yy in range(y, y + size):
                for xx in range(x, x + size):
                    if grid[yy][xx] != GRASS:
                        good = False
                        break

                if not good:
                    break

            if good:
                return True

    return False


def validate(grid):
    # player 1 can reach starting gold
    if not reachable(grid, P1, target_ring(*GOLD1, MINE_W, MINE_H)):
        return False

    # player 2 can reach starting gold
    if not reachable(grid, P2, target_ring(*GOLD2, MINE_W, MINE_H)):
        return False

    # both can reach extra gold
    if not reachable(grid, P1, target_ring(*MID_GOLD1, MINE_W, MINE_H)):
        return False

    if not reachable(grid, P2, target_ring(*MID_GOLD2, MINE_W, MINE_H)):
        return False

    # player 1 can reach player 2
    if not reachable(grid, P1, target_ring(*TH2, TOWNHALL_W, TOWNHALL_H)):
        return False

    # player 2 can reach player 1
    if not reachable(grid, P2, target_ring(*TH1, TOWNHALL_W, TOWNHALL_H)):
        return False

    # enough room for buildings
    if not has_build_space(grid, P1):
        return False

    if not has_build_space(grid, P2):
        return False

    return True


def make_candidate(seed, attempt):
    rng = random.Random(seed + attempt * 1000003)

    grid = initial_grid(rng)

    for _ in range(CA_STEPS):
        grid = ca_step(grid)

    clear_gameplay_zones(grid)

    return grid


def generate(seed):
    for attempt in range(MAX_ATTEMPTS):
        grid = make_candidate(seed, attempt)

        if validate(grid):
            return grid, attempt

    raise RuntimeError(
        "could not generate a playable map after %d attempts" % MAX_ATTEMPTS
    )


def add_entity(lines, entity_id, entity_type, fields):
    lines.append(f'\t<entity id="{entity_id}">')
    lines.append(f"\t\t<type>{entity_type}</type>")

    for name, value in fields:
        lines.append(f"\t\t<{name}>{value}</{name}>")

    lines.append("\t</entity>")


def write_xml(grid, output_path):
    lines = ["<gamestate>"]

    lines.append('\t<entity id="0">')
    lines.append("\t\t<type>map</type>")
    lines.append(f"\t\t<width>{WIDTH}</width>")
    lines.append(f"\t\t<height>{HEIGHT}</height>")
    lines.append("\t\t<background>")

    for row in grid:
        lines.append("\t\t\t<row>" + "".join(row) + "</row>")

    lines.append("\t\t</background>")
    lines.append("\t</entity>")

    add_entity(
        lines,
        1,
        "WPlayer",
        [
            ("gold", 2000),
            ("wood", 1500),
            ("owner", "player1"),
        ],
    )

    add_entity(
        lines,
        2,
        "WPlayer",
        [
            ("gold", 2000),
            ("wood", 1500),
            ("owner", "player2"),
        ],
    )

    add_entity(
        lines,
        3,
        "WTownhall",
        [
            ("x", TH1[0]),
            ("y", TH1[1]),
            ("owner", "player1"),
            ("current_hitpoints", 2400),
        ],
    )

    add_entity(
        lines,
        4,
        "WPeasant",
        [
            ("x", P1[0]),
            ("y", P1[1]),
            ("owner", "player1"),
            ("current_hitpoints", 30),
        ],
    )

    add_entity(
        lines,
        5,
        "WTownhall",
        [
            ("x", TH2[0]),
            ("y", TH2[1]),
            ("owner", "player2"),
            ("current_hitpoints", 2400),
        ],
    )

    add_entity(
        lines,
        6,
        "WPeasant",
        [
            ("x", P2[0]),
            ("y", P2[1]),
            ("owner", "player2"),
            ("current_hitpoints", 30),
        ],
    )

    add_entity(
        lines,
        7,
        "WGoldMine",
        [
            ("x", GOLD1[0]),
            ("y", GOLD1[1]),
            ("remaining_gold", 50000),
            ("current_hitpoints", 25500),
        ],
    )

    add_entity(
        lines,
        8,
        "WGoldMine",
        [
            ("x", GOLD2[0]),
            ("y", GOLD2[1]),
            ("remaining_gold", 50000),
            ("current_hitpoints", 25500),
        ],
    )

    add_entity(
        lines,
        9,
        "WGoldMine",
        [
            ("x", MID_GOLD1[0]),
            ("y", MID_GOLD1[1]),
            ("remaining_gold", 100000),
            ("current_hitpoints", 25500),
        ],
    )

    add_entity(
        lines,
        10,
        "WGoldMine",
        [
            ("x", MID_GOLD2[0]),
            ("y", MID_GOLD2[1]),
            ("remaining_gold", 100000),
            ("current_hitpoints", 25500),
        ],
    )

    lines.append("</gamestate>")

    output_path.parent.mkdir(parents=True, exist_ok=True)

    output_path.write_text("\n".join(lines) + "\n", encoding="utf-8")


def main():
    parser = argparse.ArgumentParser(
        description=("generate a playable S3 map " "with a 2-D cellular automaton")
    )

    parser.add_argument(
        "--seed",
        type=int,
        default=387,
        help="random seed; same seed gives the same map",
    )

    parser.add_argument(
        "--output",
        default="maps/map.xml",
        help="output XML file",
    )

    args = parser.parse_args()

    grid, attempt = generate(args.seed)

    output = Path(args.output)
    write_xml(grid, output)

    print(f"generated {output} " f"with seed {args.seed} " f"(candidate {attempt})")


if __name__ == "__main__":
    main()
