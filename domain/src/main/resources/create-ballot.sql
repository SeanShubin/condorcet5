insert into ballot (election_id, user_id, confirmation, when_cast)
values ((select id from election where name = ?),
        (select id from user where name = ?),
        ?,
        ?)
