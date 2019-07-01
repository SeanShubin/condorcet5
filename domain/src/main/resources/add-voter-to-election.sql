insert into voter (election_id, user_id)
values ((select id from election where name = ?),
        (select id from user where name = ?))
