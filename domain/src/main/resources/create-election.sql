insert into election (owner_id,
                      name,
                      end,
                      secret,
                      status_id)
values ((select id from user where name = ?),
        ?,
        ?,
        ?,
        (select id from status where name = ?))
