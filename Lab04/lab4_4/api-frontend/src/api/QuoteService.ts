import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api/v1";

interface QuoteRequest {
    quote: string;
    movieId: string;
}

export const QuoteService = {
    createQuote: (quoteRequest: QuoteRequest) =>
        axios.post(`${API_BASE_URL}/quotes`, quoteRequest),
    getQuoteById: (id: string) => axios.get(`${API_BASE_URL}/quotes/${id}`),
    getAllQuotes: (movieId?: string) => {
        const params = movieId ? { movieId } : {};
        return axios.get(`${API_BASE_URL}/quotes`, { params });
    },
    getRandomQuote: () => axios.get(`${API_BASE_URL}/quote`),
    updateQuote: (id: string, quoteRequest: QuoteRequest) =>
        axios.put(`${API_BASE_URL}/quotes/${id}`, quoteRequest),
    deleteQuote: (id: string) => axios.delete(`${API_BASE_URL}/quotes/${id}`),
};
