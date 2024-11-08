interface SquareProps {
  value: string;
  onSquareClick: () => void;
}

export default function Square({ value, onSquareClick }: SquareProps) {
  const squareStyle = {
    backgroundColor: value === 'X' ? 'lightcoral' : value === 'O' ? 'blue' : 'white'
  };
  return (
    <button className="square" onClick={onSquareClick} style={squareStyle}>
      {value}
    </button>
  );
}