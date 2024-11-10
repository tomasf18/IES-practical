import { useEffect, useState } from "react";
import { MovieService } from "../api/MovieService";

interface Movie {
    id: string;
    title: string;
    year: string;
}

function MoviePage() {
    const [movies, setMovies] = useState<Movie[]>([]);
    const [title, setTitle] = useState("");
    const [year, setYear] = useState("");
    const [searchYear, setSearchYear] = useState("");
    const [searchId, setSearchId] = useState("");
    const [editMode, setEditMode] = useState(false);
    const [editMovieId, setEditMovieId] = useState<string | null>(null);

    useEffect(() => {
        fetchMovies();
    }, []);

    const fetchMovies = async () => {
        try {
            const movies = await MovieService.getAllMovies();
            setMovies(movies);
        } catch (error) {
            console.error("Error fetching movies:", error);
        }
    };

    const handleAddMovie = async () => {
        const newMovie = { title, year };
        try {
            await MovieService.createMovie(newMovie);
            alert("Movie created successfully");
            setTitle("");
            setYear("");
            fetchMovies();
        } catch (error) {
            console.error("Error creating movie:", error);
            alert("Failed to create movie");
        }
    };

    const handleEditMovie = async () => {
        if (editMovieId) {
            const updatedMovie = { id: editMovieId, title, year };
            try {
                await MovieService.updateMovie(editMovieId, updatedMovie);
                alert("Movie updated successfully");
                setEditMode(false);
                setEditMovieId(null);
                setTitle("");
                setYear("");
                fetchMovies();
            } catch (error) {
                console.error("Error updating movie:", error);
                alert("Failed to update movie");
            }
        }
    };

    const handleDeleteMovie = async (id: string) => {
        try {
            await MovieService.deleteMovie(id);
            alert("Movie deleted successfully");
            fetchMovies();
        } catch (error) {
            console.error("Error deleting movie:", error);
            alert("Failed to delete movie");
        }
    };

    const handleSearchById = async () => {
        try {
            const movie = await MovieService.getMovieById(searchId);
            setMovies([movie]);
        } catch (error) {
            console.error("Error searching movie by ID:", error);
            alert("Failed to find movie by ID");
        }
    };

    const handleFilterByYear = async () => {
        try {
            const movies = await MovieService.getAllMovies(searchYear);
            setMovies(movies);
        } catch (error) {
            console.error("Error filtering movies by year:", error);
            alert("Failed to filter movies");
        }
    };

    return (
        <div className="container">
            <h1 className="header">Movies</h1>

            <div className="content">
                {/* Left Column - Operations */}
                <div className="operations">
                    <h2 className="subHeader">
                        {editMode ? "Edit Movie" : "Add Movie"}
                    </h2>
                    <input
                        className="input"
                        placeholder="Title"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                    />
                    <input
                        className="input"
                        placeholder="Year"
                        value={year}
                        onChange={(e) => setYear(e.target.value)}
                    />
                    <button
                        className="button"
                        onClick={editMode ? handleEditMovie : handleAddMovie}
                    >
                        {editMode ? "Update Movie" : "Add Movie"}
                    </button>
                    {editMode && (
                        <button
                            className="button cancelButton"
                            onClick={() => {
                                setEditMode(false);
                                setEditMovieId(null);
                                setTitle("");
                                setYear("");
                            }}
                        >
                            Cancel Edit
                        </button>
                    )}

                    <h2 className="subHeader">Filter Movies by Year</h2>
                    <input
                        className="input"
                        placeholder="Year"
                        value={searchYear}
                        onChange={(e) => setSearchYear(e.target.value)}
                    />
                    <button className="button" onClick={handleFilterByYear}>
                        Filter
                    </button>

                    <h2 className="subHeader">Search Movie by ID</h2>
                    <input
                        className="input"
                        placeholder="Movie ID"
                        value={searchId}
                        onChange={(e) => setSearchId(e.target.value)}
                    />
                    <button className="button" onClick={handleSearchById}>
                        Search
                    </button>
                </div>

                {/* Right Column - Movie List */}
                <div className="movieList">
                    <h2 className="subHeader">All Movies</h2>
                    <button className="button" onClick={fetchMovies}>
                        Get All Movies
                    </button>
                    <ul>
                        {movies.map((movie) => (
                            <li key={movie.id} className="listItem">
                                {movie.id} - {movie.title} ({movie.year})
                                <div>
                                    <button
                                        className="actionButton"
                                        onClick={() => {
                                            setEditMode(true);
                                            setEditMovieId(movie.id);
                                            setTitle(movie.title);
                                            setYear(movie.year);
                                        }}
                                    >
                                        Edit
                                    </button>
                                    <button
                                        className="actionButton"
                                        onClick={() =>
                                            handleDeleteMovie(movie.id)
                                        }
                                    >
                                        Delete
                                    </button>
                                </div>
                            </li>
                        ))}
                    </ul>
                </div>
            </div>
        </div>
    );
}

export default MoviePage;
