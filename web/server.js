//uvoz modula
const express = require('express');
const app = express();
const path = require('path');
const pg = require('pg')
const db = require('./db')
const session = require('express-session')
const pgSession = require('connect-pg-simple')(session)


//uvoz modula s definiranom funkcionalnosti ruta
const homeRouter = require('./routes/home.routes');
const loginRoute = require('./routes/login.routes');
const logoutRoute = require('./routes/logout.routes');
const signupRoute = require('./routes/signup.routes');
const userRoute = require('./routes/user.routes');
const gameCodeRoute = require('./routes/gamecode')

//middleware - predlošci (ejs)
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

//middleware - statički resursi
app.use(express.static(path.join(__dirname, 'public')));

//middleware - dekodiranje parametara
app.use(express.urlencoded({extended: true}));


//pohrana sjednica u postgres bazu korštenjem connect-pg-simple modula
app.use(session({
    secret: 'uciteljica',
    resave: false,
    store: new pgSession({
        pool: db.pool
    }),
    saveUninitialized: true
}));


//definicija ruta
app.use('/', homeRouter);
app.use('/login', loginRoute);
app.use('/logout', logoutRoute);
app.use('/signup', signupRoute);
app.use('/user', userRoute);
app.use('/gamecode', gameCodeRoute);

//pokretanje poslužitelja na portu 3000
app.listen(3000);

