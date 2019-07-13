select tally.id,
       election.name election,
       tally.report,
       tally.election_id
from tally
         inner join election on tally.election_id = election.id
order by tally.id
