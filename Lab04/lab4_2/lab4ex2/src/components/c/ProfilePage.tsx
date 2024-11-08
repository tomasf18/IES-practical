import AllPosts from './AllPosts';
import Heading from './Heading';
import Post from './Post';
import Section from './Section';

export default function ProfilePage() {
    return (
      <Section isFancy={true}>
        <Heading>My Profile</Heading>
        <Post
          title="Hello traveller!"
          body="Read about my adventures."
        />
        <AllPosts />
      </Section>
    );
  }