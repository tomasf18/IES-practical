import Heading from "./Heading";
import Section from "./Section";

interface PostProps {
  title: string;
  body: string;
}

function Post({ title, body }: PostProps) {
    return (
      <Section isFancy={true}>
        <Heading>
          {title}
        </Heading>
        <p><i>{body}</i></p>
      </Section>
    );
  }

export default Post;