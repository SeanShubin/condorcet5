update
    election
set status_id = (select id from status where name = ?)
where name = ?
