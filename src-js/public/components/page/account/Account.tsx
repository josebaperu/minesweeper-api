import React, { useEffect, useState } from 'react';
import { useCookies } from 'react-cookie';
import './Accounts.scss';
import { useHistory } from 'react-router-dom';
import { API_SERVER } from '../../../Constant';
const  Account = (props: any) => {
    const [cookies, setCookie] = useCookies(['minesweeper']);
    const [isSignUp, setIsSignUP] = useState(true);
    const [userExist, setUserExist] = useState(false);
    const history = useHistory();

    const usernameRef = React.createRef();
    const passwordRef = React.createRef();

    const onHandleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (isSignUp) {
            // @ts-ignore
            fetch(`${API_SERVER}/api/user/exist/${usernameRef.current.value}`)
                .then(response => response.json())
                .then(response => {

                    if (response.userExist) {
                        alert('User Exist');
                    } else if (isSignUp && !userExist) {
                        // @ts-ignore
                        fetch(`${API_SERVER}/api/user/new`, {
                            method: 'POST',
                            headers: {
                                'Accept': 'application/json',
                                'Content-Type': 'application/json'
                            },
                            // tslint:disable-next-line:max-line-length
                            body: JSON.stringify({username: usernameRef.current.value, password: passwordRef.current.value})
                            // tslint:disable-next-line:no-shadowed-variable
                        }).then(response => response.json())
                            // tslint:disable-next-line:no-shadowed-variable
                            .then(response => {
                                if (response.token) {
                                    alert('User Succesfully Created');
                                }
                        });
                    }
                });
        }
        if (!isSignUp) {
            // @ts-ignore
            fetch(`${API_SERVER}/api/user/signin/`, {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                // tslint:disable-next-line:max-line-length
                body: JSON.stringify({username: usernameRef.current.value, password: passwordRef.current.value})
            }).then(response => response.json())
                .then(response => {
                    if (response.token) {
                        setCookie('token', response.token);
                        setCookie('username', response.username);
                        history.push('/dashboard');
                    } else if (!response.userExist) {
                        setCookie('token', '');
                        setCookie('username', '');
                        alert('User does not exist');
                    }
                });
        }
    };

    const renderForm = () => {
        // @ts-ignore
        return <>
            <h2 className={'headline'}>{isSignUp ? 'Account Sign Up' : 'Account Sign In'}</h2>
            <form>
                <label>Username:<input type='text' ref={usernameRef} /></label>< br/>
                <label>Password:<input type='password' ref={passwordRef}/></label>< br/>
                {/* tslint:disable-next-line:max-line-length */}
                <input className={'buttonSubmit'} type='button' value={isSignUp ? 'Sign Up' : 'Sign In'} onClick={e => {onHandleSubmit(e); }}/>
            </form>
        </>;
    };
    return (
        <div className={'account'}>
            {renderForm()}
            {<button className={'formSwitcherButton'} onClick={() => {setIsSignUP(!isSignUp); }}>{isSignUp ? 'Sign In Form' : 'Sign Up Form'}</button>}
        </div>
    );
};
export default Account;
