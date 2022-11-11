import sys

author_id = 0

def main():
    books = open(sys.argv[1], "r", encoding="utf-8")
    borrowers = open(sys.argv[2], "r", encoding="utf-8")
    out = open("populate.sql", "w", encoding="utf-8")
    readFile(books, "\t", out, constructBookAndAuthorInserts)
    readFile(borrowers, ",", out, constructBorrowerAndLoanInserts)

# reads the file line by line and extracts the values delimited by the 
# given character. Then passes delimited values to processing method.
def readFile(file, delim, output, extract):
    index = -1
    for line in file:
        index += 1
        if (index == 0):
            continue
        values = line.split(delim)
        extract(values, output)

# constructs insert statements to populate library.books
def insertBooks(isbn, title, output):
    output.write(
        "INSERT INTO Library.Book VALUES (" 
        + isbn + ", " + title + ");\n"
    )

# constructs insert statements to populate library.authors
def insertAuthors(author_id, author, output):
        name = "\"" + author + "\""
        output.write(
            "INSERT INTO Library.authors VALUES (" 
            + str(author_id) + ", " + name + ");\n"
        )

# constructs insert statements to populate library.book_authors
def insertBook_Authors(author_id, isbn, output):
        output.write(
            "INSERT INTO Library.book_authors VALUES (" 
            + str(author_id) + ", " + isbn + ");\n"
        )

# constructs insert statements for books and authors 
# in the library database
def constructBookAndAuthorInserts(record, output):
    global author_id

    # isbn13 must be non-null
    if (len(record[1]) == 0):
        print("Rejected (no isbn13): " + str(record))
        return

    isbn = (
        "\"" +
        record[1][0:3] + "-" + record[1][3] + "-" + 
        record[1][4:9] + "-" + record[1][9:12] + "-" + 
        record[1][12]
        + "\""
    )

    # title must be non-null
    if (len(record[2]) == 0):
        print("Rejected (no title): " + str(record))
        return
    title = "\"" + record[2] + "\""

    # author must also be non null
    if (len(record[3]) == 0):
        print("Rejected (no author): " + str(record))
        return

    authors = record[3].split(",")

    insertBooks(isbn, title, output)

    for author in authors:
        insertAuthors(author_id, author, output)
        insertBook_Authors(author_id, isbn, output)
        author_id += 1

# constructs insert statements for borrowers, loans and 
# fines in the library database
def constructBorrowerAndLoanInserts(record, output):
    borrower_id = record[0][2:8]
    ssn = "\"" + record[1][0:2] + record[1][4:5] + record[1][7:10] + "\""
    name = "\"" + record[2] + " " + record[3] + "\""
    address = "\"" + record[5] + ", " + record[6] + ", " + record[7] + "\""
    phone = "\"" + record[8][:len(record[8]) - 1] + "\""

    output.write(
        "INSERT INTO Library.Borrower VALUES ("
        + str(borrower_id) + ", " + ssn + ", " + name
        + ", " + address + ", " + phone + ");\n"
    )

main()