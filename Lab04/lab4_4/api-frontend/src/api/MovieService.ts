import AxiosInstance from './AxiosInstance';

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
    createMovie: async (movie: MovieCreate) => {
        try {
            const response = await AxiosInstance.post<Movie>('/movies', movie);
            return response.data;
        } catch (error) {
            console.error('Error creating movie:', error);
            throw error;
        }
    },

    getMovieById: async (id: string) => {
        try {
            const response = await AxiosInstance.get<Movie>(`/movies/${id}`);
            return response.data;
        } catch (error) {
            console.error('Error fetching movie by ID:', error);
            throw error;
        }
    },

    getAllMovies: async (year?: string) => {
        try {
            const params = year ? { year } : {};
            const response = await AxiosInstance.get<Movie[]>('/movies', { params });
            return response.data;
        } catch (error) {
            console.error('Error fetching movies:', error);
            throw error;
        }
    },

    updateMovie: async (id: string, movie: Movie) => {
        try {
            const response = await AxiosInstance.put<Movie>(`/movies/${id}`, movie);
            return response.data;
        } catch (error) {
            console.error('Error updating movie:', error);
            throw error;
        }
    },

    deleteMovie: async (id: string) => {
        try {
            await AxiosInstance.delete(`/movies/${id}`);
            return { success: true };
        } catch (error) {
            console.error('Error deleting movie:', error);
            throw error;
        }
    },
};
