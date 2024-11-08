interface Person {
    name: string;
    imageId: string;
}

export function getImageUrl(person: Person, size: string = 's'): string {
    return (
        'https://i.imgur.com/' +
        person.imageId +
        size +
        '.jpg'
    );
}
  