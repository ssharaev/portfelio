CREATE TABLE accounts (
                          id uuid,
                          userId uuid,
                          type symbol,
                          createdAt timestamp
) timestamp (createdAt) PARTITION BY DAY WAL
DEDUP UPSERT KEYS(createdAt, id);

CREATE TABLE IF NOT EXISTS positions (
                                         id uuid,
                                         accountId uuid,
                                         Ticker SYMBOL capacity 256 CACHE,
                                         quantity double,
                                         Timestamp TIMESTAMP
) timestamp (Timestamp) PARTITION BY MONTH WAL
DEDUP UPSERT KEYS(id, Timestamp, Ticker);

CREATE TABLE IF NOT EXISTS transactions (
                                            id uuid,
                                            type symbol capacity 4 CACHE,
                                            status symbol capacity 4 CACHE,
                                            currency symbol capacity 256 CACHE,
                                            accountId uuid,
                                            instrumentId uuid,
                                            quantity double,
                                            fee double,
                                            total double,
                                            Timestamp TIMESTAMP
) timestamp (Timestamp) PARTITION BY MONTH WAL
DEDUP UPSERT KEYS(id, Timestamp);

CREATE TABLE IF NOT EXISTS instruments (
                                           id uuid,
                                           exchangeId uuid,
                                           ticker symbol capacity 256 CACHE,
                                           name string,
                                           currency symbol capacity 256 CACHE,
                                           timestamp TIMESTAMP
) timestamp (timestamp) PARTITION BY MONTH WAL
DEDUP UPSERT KEYS(id, timestamp);

CREATE TABLE IF NOT EXISTS nasdaq_open_close (
                                                 Ticker SYMBOL capacity 256 CACHE,
                                                 Open DOUBLE,
                                                 High DOUBLE,
                                                 Low DOUBLE,
                                                 Close DOUBLE,
                                                 AdjClose DOUBLE,
                                                 Volume LONG,
                                                 Timestamp TIMESTAMP
) timestamp (Timestamp) PARTITION BY MONTH WAL
DEDUP UPSERT KEYS(Timestamp, Ticker);

with nasd as (select distinct Ticker from nasdaq_open_close)
INSERT INTO instruments (id, exchangeId, ticker, name, currency, timestamp) SELECT
                                                                                rnd_uuid4(),
                                                                                '018cb9b6-6130-780a-81e7-886edf55c51f',
                                                                                Ticker,
                                                                                Ticker,
                                                                                'USD',
                                                                                now()
FROM nasd;

insert into accounts
(id, userId, type, createdAt)
VALUES (
           '23416940-e906-4d2d-b1e2-11c7adcd78f6',
           '018cd812-628e-7069-adeb-cb8711c5f1cb',
           'MARKET',
           to_timestamp('2017-09-26', 'YYYY-MM-dd')
       );

INSERT into transactions
select
    rnd_uuid4() id,
    'INITIAL' trType,
    'SUCCESS' status,
    'USD' curr,
    '23416940-e906-4d2d-b1e2-11c7adcd78f6' accId,
    i.id instrId,
    1 quantity,
    0 fee,
    n.Close,
    n.Timestamp
from nasdaq_open_close n JOIN instruments i on n.Ticker = i.ticker
where n.Timestamp = to_timestamp('2017-09-26', 'YYYY-MM-dd');

