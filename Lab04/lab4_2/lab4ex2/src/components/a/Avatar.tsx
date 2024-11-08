import { getImageUrl } from './utils.ts';

interface Person {
    name: string;
    imageId: string;
}

export default function Avatar({person, size} : {person: Person, size: number}) {
  return (
    <img
      className="avatar"
      src={getImageUrl(person)}
      alt={person.name}
      width={size}
      height={size}
    />
  );
}
