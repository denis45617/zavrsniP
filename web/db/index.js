const {Pool} = require('pg');

const pool = new Pool({
    user: 'jsuayblazmifbs',
    host: 'ec2-63-32-248-14.eu-west-1.compute.amazonaws.com',
    database: 'dukn9pneia7nn',
    password: '2ea916eff41af407d3a204690f9ac97b6902fe8e466b7ab0e93fb94d51dfafba',
    port: 5432,
    ssl: {
        rejectUnauthorized: false
    }

});

module.exports = {
    query: (text, params) => {
        return pool.query(text, params)
            .then(res => {
                return res;
            });
    },
    pool: pool
}
