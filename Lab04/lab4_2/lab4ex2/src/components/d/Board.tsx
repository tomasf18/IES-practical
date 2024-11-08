import Square from "./Square";
import calculateWinner from "../../utils/calculateWinner";

type BoardProps = {
  xIsNext: boolean;
  squares: string[];
  onPlay: (squares: string[]) => void;
};

export default function Board({ xIsNext, squares, onPlay }: BoardProps) {
  function handleClick(i: number) {
    if (calculateWinner(squares) || squares[i]) {
      return;
    }
    const nextSquares = squares.slice();
    if (xIsNext) {
      nextSquares[i] = 'X';
    } else {
      nextSquares[i] = 'O';
    }
    onPlay(nextSquares);
  }

  const winner = calculateWinner(squares);
  let status;
  if (winner) {
    status = winner + ' Wins!';
  } else {
    status = 'Next player: ' + (xIsNext ? 'X' : 'O');
  }

  return (
    <>
      <div className="game-info">{status}</div>
      {[...Array(6)].map((_, row) => (
        <div className="board-row" key={row}>
          {[...Array(6)].map((_, col) => (
            <Square
              key={col}
              value={squares[row * 6 + col]}
              onSquareClick={() => handleClick(row * 6 + col)}
            />
          ))}
        </div>
      ))}
    </>
  );
}
