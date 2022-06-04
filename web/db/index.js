const {Pool} = require('pg');

const pool = new Pool({
    user: 'ykpyzqgeelnxyc',
    host: 'ec2-54-76-43-89.eu-west-1.compute.amazonaws.com',
    database: 'dchi5ckjqta9rb',
    password: '7f7e6a7b76dd716937fc32628841b46cf96782293d05b54f2e2bb731077b35d3',
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
