insert into voter (election_id, user_id)
select (select id from election where name = ?),
       user.id
from user
