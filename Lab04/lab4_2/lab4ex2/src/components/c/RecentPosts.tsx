import Heading from "./Heading";
import Post from "./Post";
import Section from "./Section";

function RecentPosts() {
    return (
      <Section isFancy={true}>
        <Heading>Recent Posts</Heading>
        <Post
          title="Flavors of Lisbon"
          body="...those pastéis de nata!"
        />
        <Post
          title="Buenos Aires in the rhythm of tango"
          body="I loved it!"
        />
      </Section>
    );
  }

export default RecentPosts;