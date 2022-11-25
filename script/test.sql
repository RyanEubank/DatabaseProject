UPDATE Library.Fines AS f 
JOIN Library.Book_Loans AS b ON f.loan_id = b.loan_id
SET f.fine_amt = f.fine_amt + 0.25
WHERE b.date_in IS NULL;