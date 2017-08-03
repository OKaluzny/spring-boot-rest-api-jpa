SELECT books.book_name, authors.author_name, publishers.publisher_name
FROM (books JOIN authors
ON books.author_fk = authors.id)
JOIN publishers
ON books.publisher_fk = publishers.id

SELECT authors.author_name, count(authors.author_name) AS Number_of_books
FROM (books JOIN authors
ON books.author_fk = authors.id)
JOIN publishers
ON books.publisher_fk = publishers.id
GROUP BY authors.author_name
HAVING count(authors.author_name) > 1
ORDER BY authors.author_name
