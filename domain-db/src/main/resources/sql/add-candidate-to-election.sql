insert into candidate (election_id, name)
values ((select id from election where name = ?),
        ?)

