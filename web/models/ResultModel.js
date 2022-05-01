const db = require('../db')


module.exports = class GameResult {

    //konstruktor korisnika
    constructor(result_id, highscore, player_nickname, result, game_code) {
        this.result_id = undefined
        this.highscore = highscore
        this.player_nickname = player_nickname
        this.result = result
        this.game_code = game_code
    }

    //dohvat korisnika na osnovu korisničkog imena
    static async fetchById(id) {
        let results = await dbGetById(id)
        let newResult = new GameResult;

        if (results.length > 0) {
            newResult = new GameResult(results[0].highscore, results[0].player_nickname, results[0].result, results[0].game_code)
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

}


//umetanje zapisa o korisniku u bazu podataka
dbGetById = async (id) => {
    const sql = `SELECT *
                 from results
                 where result_id = ${id}`;

    try {
        return await db.query(sql, []).rows;
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
