import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api/v1";

interface Movie {
    id: string;
    title: string;
    year: string;
}

interface MovieCreate {
    title: string;
    year: string;
}

export const MovieService = {
    createMovie: (movie: MovieCreate) =>
        axios.post(`${API_BASE_URL}/movies`, movie),
    getMovieById: (id: string) => axios.get(`${API_BASE_URL}/movies/${id}`),
    getAllMovies: (year?: string) => {
        const params = year ? { year } : {};
        return axios.get(`${API_BASE_URL}/movies`, { params });
    },
    updateMovie: (id: string, movie: Movie) =>
        axios.put(`${API_BASE_URL}/movies/${id}`, movie),
    deleteMovie: (id: string) => axios.delete(`${API_BASE_URL}/movies/${id}`),
};
