import { useEffect, useState } from "react";
import { QuoteService } from "../api/QuoteService";

interface Quote {
    id: string;
    quote: string;
    movieId: string;
}

function QuotePage() {
    const [quotes, setQuotes] = useState<Quote[]>([]);
    const [quoteText, setQuoteText] = useState("");
    const [movieId, setMovieId] = useState("");
    const [searchMovieId, setSearchMovieId] = useState("");
    const [searchId, setSearchId] = useState("");
    const [editMode, setEditMode] = useState(false);
    const [editQuoteId, setEditQuoteId] = useState<string | null>(null);

    useEffect(() => {
        fetchQuotes();
        QuoteService.connectWebSocket(setQuotes);
    }, []);

    const fetchQuotes = async () => {
        try {
            const quotes = await QuoteService.getAllQuotes();
            setQuotes(quotes.slice(-5));
        } catch (error) {
            console.error("Error fetching quotes:", error);
        }
    };

    const handleAddQuote = async () => {
        const newQuote = { quote: quoteText, movieId };
        try {
            await QuoteService.createQuote(newQuote);
            alert("Quote created successfully");
            setQuoteText("");
            setMovieId("");
            fetchQuotes();
        } catch (error) {
            console.error("Error creating quote:", error);
            alert("Failed to create quote");
        }
    };

    const handleEditQuote = async () => {
        if (editQuoteId) {
            const updatedQuote = { id: editQuoteId, quote: quoteText, movieId };
            try {
                await QuoteService.updateQuote(editQuoteId, updatedQuote);
                alert("Quote updated successfully");
                setEditMode(false);
                setEditQuoteId(null);
                setQuoteText("");
                setMovieId("");
                fetchQuotes();
            } catch (error) {
                console.error("Error updating quote:", error);
                alert("Failed to update quote");
            }
        }
    };

    const handleDeleteQuote = async (id: string) => {
        try {
            await QuoteService.deleteQuote(id);
            alert("Quote deleted successfully");
            fetchQuotes();
        } catch (error) {
            console.error("Error deleting quote:", error);
            alert("Failed to delete quote");
        }
    };

    const handleFilterByMovieId = async () => {
        try {
            const quotes = await QuoteService.getAllQuotes(searchMovieId);
            setQuotes(quotes);
        } catch (error) {
            console.error("Error filtering quotes by movie ID:", error);
            alert("Failed to filter quotes");
        }
    };

    const handleSearchById = async () => {
        try {
            const quote = await QuoteService.getQuoteById(searchId);
            setQuotes([quote]);
        } catch (error) {
            console.error("Error searching quote by ID:", error);
            alert("Failed to find quote by ID");
        }
    };

    const handleGetRandomQuote = async () => {
        try {
            const randomQuote = await QuoteService.getRandomQuote();
            setQuotes([randomQuote]);
        } catch (error) {
            console.error("Error fetching random quote:", error);
            alert("Failed to fetch random quote");
        }
    };

    return (
        <div className="container">
            <h1 className="header">Quotes</h1>

            <div className="content">
                {/* Left Column - Operations */}
                <div className="operations">
                    <h2 className="subHeader">
                        {editMode ? "Edit Quote" : "Add Quote"}
                    </h2>
                    <input
                        className="input"
                        placeholder="Quote Text"
                        value={quoteText}
                        onChange={(e) => setQuoteText(e.target.value)}
                    />
                    <input
                        className="input"
                        placeholder="Movie ID"
                        value={movieId}
                        onChange={(e) => setMovieId(e.target.value)}
                    />
                    <button
                        className="button"
                        onClick={editMode ? handleEditQuote : handleAddQuote}
                    >
                        {editMode ? "Update Quote" : "Add Quote"}
                    </button>
                    {editMode && (
                        <button
                            className="button cancelButton"
                            onClick={() => {
                                setEditMode(false);
                                setEditQuoteId(null);
                                setQuoteText("");
                                setMovieId("");
                            }}
                        >
                            Cancel Edit
                        </button>
                    )}

                    <h2 className="subHeader">Filter Quotes by Movie ID</h2>
                    <input
                        className="input"
                        placeholder="Movie ID"
                        value={searchMovieId}
                        onChange={(e) => setSearchMovieId(e.target.value)}
                    />
                    <button className="button" onClick={handleFilterByMovieId}>
                        Filter
                    </button>

                    <h2 className="subHeader">Search Quote by ID</h2>
                    <input
                        className="input"
                        placeholder="Quote ID"
                        value={searchId}
                        onChange={(e) => setSearchId(e.target.value)}
                    />
                    <button className="button" onClick={handleSearchById}>
                        Search
                    </button>

                    <h2 className="subHeader">Random Quote</h2>
                    <button className="button" onClick={handleGetRandomQuote}>
                        Get Random Quote
                    </button>
                </div>

                {/* Right Column - Quote List */}
                <div className="quoteList">
                    <h2 className="subHeader">All Quotes</h2>
                    <button className="button" onClick={fetchQuotes}>
                        Get All Quotes
                    </button>
                    <ul>
                        {quotes.map((quote) => (
                            <li key={quote.id} className="listItem">
                                {quote.id} - {quote.quote}
                                <div>
                                    <button
                                        className="actionButton"
                                        onClick={() => {
                                            setEditMode(true);
                                            setEditQuoteId(quote.id);
                                            setQuoteText(quote.quote);
                                            setMovieId(quote.movieId);
                                        }}
                                    >
                                        Edit
                                    </button>
                                    <button
                                        className="actionButton"
                                        onClick={() =>
                                            handleDeleteQuote(quote.id)
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

export default QuotePage;
