import { useEffect, useState } from 'react';
import { MovieService } from '../api/MovieService';

interface Movie {
  id: string;
  title: string;
  year: string;
}

function MoviePage() {
  const [movies, setMovies] = useState<Movie[]>([]);
  const [loading, setLoading] = useState(true);
  const [title, setTitle] = useState('');
  const [year, setYear] = useState('');
  const [searchYear, setSearchYear] = useState('');
  const [searchId, setSearchId] = useState('');
  const [editMode, setEditMode] = useState(false);
  const [editMovieId, setEditMovieId] = useState<string | null>(null);

  useEffect(() => {
    fetchMovies();
  }, []);

  const fetchMovies = async () => {
    setLoading(true);
    try {
      const response = await MovieService.getAllMovies();
      setMovies(response.data);
    } catch (error) {
      console.error("Error fetching movies:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleAddMovie = async () => {
    const newMovie = { title, year };
    try {
      await MovieService.createMovie(newMovie);
      alert('Movie created successfully');
      setTitle('');
      setYear('');
      fetchMovies();
    } catch (error) {
      console.error("Error creating movie:", error);
      alert('Failed to create movie');
    }
  };

  const handleEditMovie = async () => {
    if (editMovieId) {
      const updatedMovie = { id: editMovieId, title, year };
      try {
        await MovieService.updateMovie(editMovieId, updatedMovie);
        alert('Movie updated successfully');
        setEditMode(false);
        setEditMovieId(null);
        setTitle('');
        setYear('');
        fetchMovies();
      } catch (error) {
        console.error("Error updating movie:", error);
        alert('Failed to update movie');
      }
    }
  };

  const handleDeleteMovie = async (id: string) => {
    try {
      await MovieService.deleteMovie(id);
      alert('Movie deleted successfully');
      fetchMovies();
    } catch (error) {
      console.error("Error deleting movie:", error);
      alert('Failed to delete movie');
    }
  };

  const handleSearchById = async () => {
    try {
      const response = await MovieService.getMovieById(searchId);
      setMovies([response.data]);
    } catch (error) {
      console.error("Error searching movie by ID:", error);
      alert('Failed to find movie by ID');
    }
  };

  const handleFilterByYear = async () => {
    try {
      const response = await MovieService.getAllMovies(searchYear);
      setMovies(response.data);
    } catch (error) {
      console.error("Error filtering movies by year:", error);
      alert('Failed to filter movies');
    }
  };

  if (loading) return <p>Loading movies...</p>;

  return (
    <div>
      <h1>Movies</h1>

      {/* Add/Edit Movie */}
      <div>
        <h2>{editMode ? 'Edit Movie' : 'Add Movie'}</h2>
        <input
          placeholder="Title"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        />
        <input
          placeholder="Year"
          value={year}
          onChange={(e) => setYear(e.target.value)}
        />
        <button onClick={editMode ? handleEditMovie : handleAddMovie}>
          {editMode ? 'Update Movie' : 'Add Movie'}
        </button>
        {editMode && (
          <button onClick={() => { setEditMode(false); setEditMovieId(null); setTitle(''); setYear(''); }}>
            Cancel Edit
          </button>
        )}
      </div>

      {/* Filter by Year */}
      <div>
        <h2>Filter Movies by Year</h2>
        <input
          placeholder="Year"
          value={searchYear}
          onChange={(e) => setSearchYear(e.target.value)}
        />
        <button onClick={handleFilterByYear}>Filter</button>
      </div>

      {/* Search by ID */}
      <div>
        <h2>Search Movie by ID</h2>
        <input
          placeholder="Movie ID"
          value={searchId}
          onChange={(e) => setSearchId(e.target.value)}
        />
        <button onClick={handleSearchById}>Search</button>
      </div>

      {/* Movies List */}
      <div>
        <h2>All Movies</h2>
        <ul>
          {movies.map((movie) => (
            <li key={movie.id}>
              {movie.title} ({movie.year})
              <button onClick={() => {
                setEditMode(true);
                setEditMovieId(movie.id);
                setTitle(movie.title);
                setYear(movie.year);
              }}>Edit</button>
              <button onClick={() => handleDeleteMovie(movie.id)}>Delete</button>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default MoviePage;
