export default function calculateWinner(squares: (string | null)[]) {
    const size = 6;

    for (let row = 0; row < size; row++) {
      for (let col = 0; col < size - 2; col++) {
        const idx = row * size + col;
        if (
          squares[idx] &&
          squares[idx] === squares[idx + 1] &&
          squares[idx] === squares[idx + 2]
        ) {
          return squares[idx];
        }
      }
    }

    for (let col = 0; col < size; col++) {
      for (let row = 0; row < size - 2; row++) {
        const idx = row * size + col;
        if (
          squares[idx] &&
          squares[idx] === squares[idx + size] &&
          squares[idx] === squares[idx + 2 * size]
        ) {
          return squares[idx];
        }
      }
    }

    for (let row = 0; row < size - 2; row++) {
      for (let col = 0; col < size - 2; col++) {
        const idx = row * size + col;
        if (
          squares[idx] &&
          squares[idx] === squares[idx + size + 1] &&
          squares[idx] === squares[idx + 2 * (size + 1)]
        ) {
          return squares[idx];
        }
      }
    }

    for (let row = 0; row < size - 2; row++) {
      for (let col = 2; col < size; col++) {
        const idx = row * size + col;
        if (
          squares[idx] &&
          squares[idx] === squares[idx + size - 1] &&
          squares[idx] === squares[idx + 2 * (size - 1)]
        ) {
          return squares[idx];
        }
      }
    }

    return null;
  }

  