select count(id) from users; -- actual

select count(distinct id) from users; -- expected

select count(*) from book_borrow where is_returned=0;
select * from books where name='Clean Code';