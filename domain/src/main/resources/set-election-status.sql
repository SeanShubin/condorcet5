update
    election
set status_id = (select id from election where name = ?)
where name = ?
