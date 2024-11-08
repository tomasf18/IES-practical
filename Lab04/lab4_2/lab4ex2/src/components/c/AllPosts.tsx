import Heading from './Heading';
import RecentPosts from './RecentPosts';
import Section from './Section';

function AllPosts() {
    return (
      <Section isFancy={true}>
        <Heading>Posts</Heading>
        <RecentPosts />
      </Section>
    );
  }

export default AllPosts;