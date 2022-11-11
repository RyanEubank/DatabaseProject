def writeValues(values, file):
	isbn = values[1]
	title = "\"" + values[2] + "\""

	file.write("INSERT INTO Library.Book VALUES(" + isbn + ", " + title + ")\n")
	authors = values[3].split(",")
	author_id = 0

	for author in authors:
		name = "\"" + author + "\""
		file.write("INSERT INTO Library.authors VALUES(" + str(author_id) + ", " + name + ");\n")
		file.write("INSERT INTO Library.book_authors VALUES(" + str(author_id) + ", " + isbn + ");\n")
		author_id += 1

	file.write("\n")

import sys

csv = open(sys.argv[1], "r", encoding="utf-8")
out = open("populate.sql", "w", encoding="utf-8")
index = -1

for line in csv:
	index += 1
	if (index == 0):
		continue
	values = line.split("\t")
	writeValues(values, out)
