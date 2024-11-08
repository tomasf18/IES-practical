import { useEffect, useState } from "react";

interface PostInfo {
    id: number;
    title: string;
    body: string;
}

function Fetch() {
    const [title, setTitle] = useState("");
    const [body, setBody] = useState("");
    const [posts, setPosts] = useState<PostInfo[]>([]);

    useEffect(() => {
        const fetchPost = async () => {
            try {
                const response = await fetch(
                    "https://jsonplaceholder.typicode.com/posts?_limit=10"
                );
                const data = await response.json();
                setPosts(data);
            } catch (error) {
                console.log(error);
            }
        };

        fetchPost();
        
    }, []);

    const deletePost = async (id: number) => {
        let response = await fetch(
            `https://jsonplaceholder.typicode.com/posts/${id}`,
            {
                method: "DELETE",
            }
        );
        if (response.status === 200) {
            setPosts(
                posts.filter((post) => {
                    return post.id !== id;
                })
            );
        } else {
            return;
        }
    };

    const addPosts = async (title: string, body: string) => {
        let response = await fetch(
            "https://jsonplaceholder.typicode.com/posts",
            {
                method: "POST",
                body: JSON.stringify({
                    title: title,
                    body: body,
                    userId: Math.random().toString(36).slice(2),
                }),
                headers: {
                    "Content-type": "application/json; charset=UTF-8",
                },
            }
        );
        let data = await response.json();
        setPosts((posts) => [data, ...posts]);
        setTitle("");
        setBody("");
    };

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        addPosts(title, body);
    };

    return (
        <div className="app">
            <div className="add-post-container">
                <form onSubmit={handleSubmit}>
                    <input
                        type="text"
                        className="form-control"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                    />
                    <textarea
                        name=""
                        className="form-control"
                        id=""
                        cols={10}
                        rows={8}
                        value={body}
                        onChange={(e) => setBody(e.target.value)}
                    ></textarea>
                    <button type="submit">Add Post</button>
                </form>
            </div>
            <div className="posts-container">
                {posts.map((post) => {
                    return (
                        <div className="post-card" key={post.id}>
                            <h2 className="post-title">{post.title}</h2>
                            <p className="post-body">{post.body}</p>
                            <div className="button">
                                <button
                                    className="delete-btn"
                                    onClick={() => deletePost(post.id)}
                                >
                                    Delete
                                </button>
                            </div>
                        </div>
                    );
                })}
            </div>
        </div>
    );
};

export default Fetch;
