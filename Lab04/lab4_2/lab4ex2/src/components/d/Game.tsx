import { useState } from "react";
import Board from "./Board";

export default function Game() {
    const [history, setHistory] = useState([Array(9).fill(null)]);
    const [currentMove, setCurrentMove] = useState(0);
    const xIsNext = currentMove % 2 === 0;
    const currentSquares = history[currentMove];
  
    function handlePlay(nextSquares: (string | null)[]) {
      const nextHistory = [...history.slice(0, currentMove + 1), nextSquares];
      setHistory(nextHistory);
      setCurrentMove(nextHistory.length - 1);
    }
  
    function jumpTo(nextMove: number) {
      setCurrentMove(nextMove);
    }
  
    const moves = history.map((_, move) => {
      let description;
      if (move > 0) {
        description = 'Go to move #' + move;
      } else {
        description = 'Go to game start';
      }
      return (
        <li key={move}>
          <button onClick={() => jumpTo(move)}>{description}</button>
        </li>
      );
    });

    function resetGame() {
      setHistory([Array(36).fill(null)]);
      setCurrentMove(0);
    }
  
    return (
      <div className="game">
        <div className="game-info">
          <h1>Jogo do Galo</h1>
          <button onClick={resetGame}>Reset Game</button>
        </div>
        <div className="game-board">
          <Board xIsNext={xIsNext} squares={currentSquares} onPlay={handlePlay} />
        </div>
        <div className="move-list">
          <ol>{moves}</ol>
        </div>
      </div>
    );
}