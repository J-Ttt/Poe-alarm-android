


with open("currency_end_data.txt", "r") as kohde:
    topFive = [(0,0), (0,0), (0,0), (0,0), (0,0), (0,0), (0,0), (0,0), (0,0), (0,0),]
    for line in kohde.readlines():
        line = line.split(" ")
        if len(line) == 2:
            break
        amount = int(line[-2])
        if len([*filter(lambda x: x[0] <= amount, topFive)]) > 0:
            topFive.append((amount, line[0]))
            topFive.sort(key=lambda tup: tup[0])
            topFive.reverse()
            topFive = topFive[:-1]
print(topFive)
