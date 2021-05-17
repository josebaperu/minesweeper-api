import React, { useState } from 'react';
import Table from '../../table/Table';
import { useCookies } from 'react-cookie';

const  Dashboard = (props: any) => {
    const [row, setRow] = useState(-1);
    const [cookies, setCookie] = useCookies(['minesweeper']);
    const handleOnClick = (rowNumber: number): void => {
        setRow(rowNumber);
    };
    return (
        <div className={'dashboard'}>
            <h2>Bienvenido {cookies.username}</h2>
            <div>{[6, 8, 10, 12, 16].map((e, i) => {
                return <button key={i} onClick={ () => {handleOnClick(e); }}>{`New Game ${e} x ${e}`}</button>
            })}
            </div>
            {row !== -1 &&
            <div>
                <Table row={row}/>
            </div>
            }
        </div>
    );
};
export default Dashboard;
