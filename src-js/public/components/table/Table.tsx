import React, { useState, useEffect } from 'react';
import './Table.scss';
import { useCookies } from 'react-cookie';
import { useHistory } from 'react-router-dom';
import { API_SERVER } from '../../Constant';

interface  Box  {
    id: string;
}

interface TableProps {
    row: number;
}

const  Table = (props: TableProps) => {
    const [table, setTable] = useState<Box[]>([]);
    const [isInitial, setInitial] = useState(true);
    const [gameId, setGameid] = useState(0);
    const [youWon, setYouWon] = useState(false);
    const [gameOver, setGameOver] = useState(false);
    const [cookies, setCookie] = useCookies(['minesweeper']);
    const history = useHistory();

    useEffect(() => {
        if (isInitial) {
            fetch(`${API_SERVER}/api/game/new/${props.row}`, {
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'token': cookies.token
                },
            })
                .then(response => response.json())
                .then(response => {
                    //console.log(response);
                    setTable(JSON.parse(response.data));
                    setGameid(response.id);
                });
            setInitial(false);
        }
        if (youWon) {
            setTimeout(() => {alert('You Won this round'); setYouWon(false); }, 100);
        }
        if (gameOver) {
            setTimeout(() => {alert('You Lost the game'); setGameOver(false); }, 100);
        }
    });
    useEffect(() => {
        setInitial(true);
        history.push('/dashboard');
    },        [props.row]);
    const handleOnBoxClick = (box: Box) => {
        // tslint:disable-next-line:no-console
        const gameCanContinue = !youWon && !gameOver;

        if (gameCanContinue) {
            fetch(`${API_SERVER}/api/game/update/${gameId}/${box.id}`, {
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'token': cookies.token
                },
            })
                .then(response =>  response.json())
                .then(response => {
                    //console.log(response);
                    setTable(JSON.parse(response.data));
                    if (response.youWon) {
                        setYouWon(true);
                    }
                    if (response.gameOver) {
                        setGameOver(true);
                    }
                });
        }
    };
    const onHandleContextMenu = (box: object, e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();
        e.target.classList.toggle('flag');
    };
    // tslint:disable-next-line:no-any
    const renderButtons = (box: object , i: number) => {
        let classNameStr = '';
        let boxValue = '\u00A0\u00A0';
        //console.log(box);
        if (box.isVisible) {
            if (box.hasBomb) {
                classNameStr = 'bomb';
            }
            if (!box.hasBomb) {
                classNameStr = 'open';
                boxValue = box.bombsAround > 0 ? box.bombsAround : '';
            }
        }
        if (youWon && box.hasBomb) {
            classNameStr = 'pink';
        }
        if (gameOver && box.hasBomb) {
            classNameStr = 'pink';
        }

        const newLine = (i + 1)  % props.row === 0;
        // tslint:disable-next-line:max-line-length
        return <span key={i}>
            {/* tslint:disable-next-line:max-line-length */}
                <span><button  onContextMenu={(e) => {onHandleContextMenu(box, e); }}onClick={() => handleOnBoxClick(box) }className={`button ${classNameStr}`}>{boxValue}</button>
                </span>{newLine && <br/>}
            </span>;
    };

    return (
        <div className={'board'}>
            {table.map((button, idx) => (
                renderButtons(button, idx)
                ))}
        </div>
    );
};
export default Table;
