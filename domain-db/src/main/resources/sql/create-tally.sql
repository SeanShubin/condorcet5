insert into tally (election_id, report)
values ((select id from election where name = ?), ?)
