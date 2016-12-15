import sys
import time
from random import shuffle

############################################
#  Created by Vishal Kulkarni on 11/29/16.
#     VVK27 | CPL635 | vvk27@njit.edu
############################################


#################
##### BOARD #####
#################
# 24 23 22 21 20
# 19 18 17 16 15
# 14 13 12 11 10
#  9  8  7  6  5
#  4  3  2  1  0
#################

seenBoards = []
solution = []
finalMoves = []
flag = False
# Total number of moves available throughout the board
moves = [] * 36


def initialize(initial):
    try:
        initial_position = initial  # sys.argv[x]  # input from user from 0 to 14
        try:
            if not (int(initial_position) <= 14 and int(initial_position) >= 0):
                raise Exception("enter arguments between following array")

        except Exception as error:
            print("#" * 60)
            print("Error:", repr(error))
            print("#" * 60)
            count = 0
            for i in range(0, 5, 1):
                for k in range(0, 5 - i, 1):
                    if (count < 10):
                        print("  ", end='', sep='', flush=True)
                for j in range(0, i + 1, 1):
                    print("(", count, ")", end='', sep='', flush=True)
                    print(" ", end='', sep='', flush=True)
                    count += 1
                print()
            sys.exit()
    except IndexError:
        print("#" * 84)
        print("Invalid Syntax: try - python3 HiqOld.py 12 {i.e. argument in between 0 to 14 inclusive}")
        print("#" * 84)
        sys.exit()

    print("# You have entered --> ", initial_position)
    print("-" * 25)
    print("     initial Board")
    print("-" * 25)

    boardBits = ""
    boardOppBits = ""
    counter = 0

    # printing the initial position of board
    for i in range(0, 5, 1):
        print("  ", end='', sep='', flush=True)
        for k in range(0, 4 - i, 1):
            print("  ", end='', sep='', flush=True)

            boardOppBits += "0"
        for j in range(0, 5, 1):
            if (j <= i):
                if (counter == int(initial_position)):
                    print("o   ", end='', sep='', flush=True)
                    boardOppBits += "1"
                else:
                    print("x   ", end='', sep='', flush=True)
                    boardOppBits += "0"
                counter += 1
        print()

    print("-" * 25)
    print("         Starting ")
    print("-" * 25)
    # print(boardOppBits)
    boardBits = int(boardOppBits, 2) ^ 1154559
    # print(boardBits)

    # initial board (one marble free in given location by user )
    INITIAL_BOARD = boardBits
    # INITIAL_BOARD = 105983
    # print(INITIAL_BOARD)

    # goal board (one marble in given location by user)
    if (int(initial_position) == 4):
        GOAL_BOARD = 4
    elif (int(initial_position) == 8):
        GOAL_BOARD = 4096
    elif (int(initial_position) == 7):
        GOAL_BOARD = 1024
    else:
        GOAL_BOARD = int(boardOppBits, 2)
    # GOAL_BOARD = 1048576
    # print(GOAL_BOARD)

    # board that contains a ball in every available slot, i.e. GOAL_BOARD | INITIAL_BOARD

    #VALID_BOARD_CELLS = GOAL_BOARD | INITIAL_BOARD
    # print(VALID_BOARD_CELLS)
    VALID_BOARD_CELLS = 1154559

    return INITIAL_BOARD, GOAL_BOARD, VALID_BOARD_CELLS


# print the board
def printBoard(board, VALID_BOARD_CELLS):
    # loop over all cells (the board is 5 x 5)
    for i in range(24, -1, -1):
        validCell = ((1 << i) & VALID_BOARD_CELLS) != 0;
        if (validCell):
            if (((1 << i) & board) != 0):
                print("  x ", end='', sep='', flush=True)
            else:
                print("  o ", end='', sep='', flush=True)
        else:
            print("  ", end='', sep='', flush=True)
        if (i % 5 == 0):
            print()


# create the two possible moves for the three added pegs
def createMoves(bit1, bit2, bit3, moves):
    possiblemoves = []
    possiblemoves.append(1 << bit1)
    possiblemoves.append((1 << bit2) | (1 << bit3))
    possiblemoves.append((1 << bit1) | (1 << bit2) | (1 << bit3))

    moves.append(possiblemoves)

    possiblemoves = []
    possiblemoves.append(1 << bit3)
    possiblemoves.append((1 << bit2) | (1 << bit1))
    possiblemoves.append((1 << bit1) | (1 << bit2) | (1 << bit3))

    moves.append(possiblemoves)


# do the calculation recursively by starting from
# the "GOAL_BOARD" and doing moves in reverse

def find_solution(board, initialboard):
    # for all possible moves
    for move in moves:
        # check if the move is valid
        if ((move[1] & board) == 0 and (move[0] & board) != 0):
            # calculate the board after this move was applied
            newBoard = board ^ move[2]
            # only continue processing if we have not seen this board before
            contains = False
            for x in range(0, len(seenBoards), 1):
                if (int(seenBoards[x]) == int(newBoard)):
                    contains = True
            if not (contains):
                seenBoards.append(newBoard)
                # check if the initial board is reached
                if (newBoard == initialboard or find_solution(newBoard, initialboard)):
                    finalMoves.append(move)
                    solution.append(board)
                    return True
    return False


def convertMove(move):
    Newmove = str(bin(move[2])[2:])
    # [print("move: " + bin(x)[2:]) for x in move]
    count = 0
    tof = []

    count1 = 5
    if (len(Newmove) >= 5):
        for x in range(len(Newmove) - 1, -1, -5):
            for y in range(x, x - count1, -1):
                if (int(Newmove[y]) == 0):
                    count += 1
                if (int(Newmove[y]) == 1):
                    count += 1
                    tof.append(15 - count)
            count1 -= 1
    else:
        for y in range(len(Newmove) - 1, -1, -1):
            if (int(Newmove[y]) == 0):
                count += 1
            if (int(Newmove[y]) == 1):
                count += 1
                tof.append(15 - count)
    print("--" * 14)
    print("[from,over,to]=", tof)
    print("--" * 14)
    return tof


def main(initialPos):
    INITIAL_BOARD, GOAL_BOARD, VALID_BOARD_CELLS = initialize(initialPos)
    while True:
        moves.clear()
        seenBoards.clear()
        solution.clear()
        finalMoves.clear()
        solution.append(INITIAL_BOARD)
        # holds all starting positions in west-east direction
        startsX = {4, 3, 2, 8, 7, 12}
        for x in startsX:
            createMoves(x, x - 1, x - 2, moves)

        # holds all starting positions in north-south direction
        startsY = {20, 15, 10, 16, 11, 12}
        for y in startsY:
            createMoves(y, y - 5, y - 10, moves)

        startsZ = {4, 8, 12, 3, 7, 2}
        for z in startsZ:
            createMoves(z, z + 4, z + 8, moves)
        shuffle(moves)
        startTime = time.time()

        find_solution(GOAL_BOARD, INITIAL_BOARD)

        i = 0
        for step in solution:
            printBoard(step, VALID_BOARD_CELLS)
            if (i < len(finalMoves)):
                # print(finalMoves[i])
                convertMove(finalMoves[i])
            i += 1
        print("-" * 26)
        print("Total No. of moves: ", len(finalMoves))
        print("-" * 44)
        print("Completed in: ", (time.time() - startTime), "seconds")
        print("-" * 44)

        if (input("Find more solutions?(yes/no): ") == "no"):
            sys.exit()
            print("+" * 45)
            print("################# Thank You #################")
            print("+" * 45)


InitialPosition = input("Enter initial position:")
if not InitialPosition == None:
    main(InitialPosition)
