import React  from 'react';
import './index.scss';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import Account from './components/page/account/Account';
import Dashboard from './components/page/dashboard/Dashboard';
import Board from './components/page/board/Board';

const  App = () => {

    return (
        <>
            <div id='page-wrap'>
                <div className='flex-main-container'>
                    <Router>
                        <Switch>
                            <Route path='/board' component={Board} />
                            <Route path='/dashboard' component={Dashboard} />
                            <Route path='/' component={Account} />
                        </Switch>
                    </Router>
                </div>
            </div>
        </>
      );
};

export default App;
