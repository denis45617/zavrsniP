const db = require('../db')


module.exports = class GameResult {

    //konstruktor rezultata
    constructor(result_id, highscore, time, player_nickname, result, game_code) {
        this.result_id = undefined
        this.highscore = highscore
        this.time = time
        this.player_nickname = player_nickname
        this.result = result
        this.game_code = game_code
    }

    //dohvat rezultat na osnovu korisničkog imena
    static async fetchById(id) {
        let results = await dbGetById(id)
        let newResult = new GameResult;

        if (results.length > 0) {
            newResult = new GameResult(undefined, results[0].highscore, results[0].time, results[0].player_nickname, results[0].result_text, results[0].game_code)
            newResult.id = results[0].id
        }
        return newResult
    }


    //pohrana rezultata u bazu podataka
    static async saveResult(highscore, player_nickname, result, game_code) {
        try {
            await dbSaveResult(highscore, player_nickname, result, game_code)
        } catch (err) {
            console.log("Error saving result " + JSON.stringify(this))
        }
    }


    //dohvaćanje svih rezultata za neki game code
    static async getResults(game_code) {
        try {
            let results = await dbGetResults(game_code);
            return results.rows;
        } catch (err) {
            console.log("Error saving result " + JSON.stringify(this))
        }
    }


    //dohvaćanje svih rezultata za neki game code
    static async getLeaderboardMobile(game_code) {
        try {
            let results = await dbGetLeaderboard(game_code);
            results = results.rows;

            let formatiraniRezultat = "";
            for (let i = 0; i < results.length; ++i) {
                formatiraniRezultat += results[i].player_nickname;
                formatiraniRezultat += ":";
                formatiraniRezultat += results[i].highscore;
                formatiraniRezultat += "#DELIMITER#";
            }

            return formatiraniRezultat;
        } catch (err) {
            console.log("Error saving result " + JSON.stringify(this))
        }
    }


}


//dohvat podataka o rezultatu po rezultatu id-a
dbGetById = async (id) => {
    const sql = `SELECT *
                 from results
                 where result_id = ${id}`;

    try {
        let result = await db.query(sql, []);
        return result.rows;
    } catch (err) {
        console.log(err);
        throw err
    }
}

//dohvat svih rezultata za neki game code
dbGetResults = async (game_code) => {
    const sql = `SELECT *
                 from results
                 where game_code = '${game_code}'
                 order by highscore`;

    try {
        return await db.query(sql, []);
    } catch (err) {
        console.log(err);
        throw err
    }
}

//umetanje zapisa o rezultatu u bazu podataka
dbSaveResult = async (highscore, player_nickname, result, game_code) => {
    const sql = `INSERT INTO results (time, highscore, player_nickname, result_text, game_code)
                 VALUES (CURRENT_TIMESTAMP, ${highscore}, '${player_nickname}', '${result}', '${game_code}')`;

    try {
        await db.query(sql, []);
    } catch (err) {
        console.log(err);
        throw err
    }
}

//dohvat svih rezultata za neki game code
dbGetLeaderboard = async (game_code) => {
    const sql = `select player_nickname, max(highscore) as highscore
                 from results
                 where game_code = '${game_code}'
                 group by player_nickname
                 order by highscore desc`;

    try {
        return await db.query(sql, []);
    } catch (err) {
        console.log(err);
        throw err
    }
}
