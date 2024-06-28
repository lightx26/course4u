// Hàm giả mạo fetchCourses
import { CourseType } from "../App.tsx";

const MockCourses: CourseType[] = [
    {
        id: 'course1',
        name: 'Introduction to Programming',
        thumbnailUrl: 'https://via.placeholder.com/150',
        assignee: {
            id: 'assignee1',
            name: 'John Doe',
            avatarUrl: 'https://via.placeholder.com/150',
        },
        platform: 'Udemy',
        period: {
            startDay: new Date('2023-01-01'),
            endDay: new Date('2023-06-01'),
        },
        rating: 4.5,
        studentNumber: 200,
    },
    {
        id: 'course2',
        name: 'Advanced Web Development',
        thumbnailUrl: 'https://via.placeholder.com/150',
        assignee: {
            id: 'assignee2',
            name: 'Jane Smith',
            avatarUrl: 'https://via.placeholder.com/150',
        },
        platform: 'Coursera',
        period: {
            startDay: new Date('2023-02-01'),
            endDay: new Date('2023-08-01'),
        },
        rating: 4.8,
        studentNumber: 150,
    },
    {
        id: 'course3',
        name: 'Data Science and Machine Learning Bootcamp',
        thumbnailUrl: 'https://via.placeholder.com/150',
        assignee: {
            id: 'assignee3',
            name: 'Alice Johnson',
            avatarUrl: 'https://via.placeholder.com/150',
        },
        platform: 'Udacity',
        period: {
            startDay: new Date('2023-03-01'),
            endDay: new Date('2023-09-01'),
        },
        rating: 4.7,
        studentNumber: 180,
    },
    {
        id: 'course4',
        name: 'Full-Stack Web Development with React',
        thumbnailUrl: 'https://via.placeholder.com/150',
        assignee: {
            id: 'assignee4',
            name: 'Bob Brown',
            avatarUrl: 'https://via.placeholder.com/150',
        },
        platform: 'Coursera',
        period: {
            startDay: new Date('2023-01-15'),
            endDay: new Date('2023-07-15'),
        },
        rating: 4.6,
        studentNumber: 220,
    },
    {
        id: 'course5',
        name: 'Cybersecurity Fundamentals',
        thumbnailUrl: 'https://via.placeholder.com/150',
        assignee: {
            id: 'assignee5',
            name: 'Cathy Green',
            avatarUrl: 'https://via.placeholder.com/150',
        },
        platform: 'edX',
        period: {
            startDay: new Date('2023-04-01'),
            endDay: new Date('2023-10-01'),
        },
        rating: 4.8,
        studentNumber: 150,
    },
    {
        id: 'course6',
        name: 'Blockchain and Cryptocurrency Explained',
        thumbnailUrl: 'https://via.placeholder.com/150',
        assignee: {
            id: 'assignee6',
            name: 'David Lee',
            avatarUrl: 'https://via.placeholder.com/150',
        },
        platform: 'Udemy',
        period: {
            startDay: new Date('2023-02-20'),
            endDay: new Date('2023-08-20'),
        },
        rating: 4.9,
        studentNumber: 300,
    },
    {
        id: 'course7',
        name: 'Introduction to Quantum Computing',
        thumbnailUrl: 'https://via.placeholder.com/150',
        assignee: {
            id: 'assignee7',
            name: 'Eva White',
            avatarUrl: 'https://via.placeholder.com/150',
        },
        platform: 'FutureLearn',
        period: {
            startDay: new Date('2023-05-01'),
            endDay: new Date('2023-11-01'),
        },
        rating: 4.4,
        studentNumber: 120,
    },
    {
        id: 'course8',
        name: 'Digital Marketing Essentials',
        thumbnailUrl: 'https://via.placeholder.com/150',
        assignee: {
            id: 'assignee8',
            name: 'Frank Black',
            avatarUrl: 'https://via.placeholder.com/150',
        },
        platform: 'LinkedIn Learning',
        period: {
            startDay: new Date('2023-03-15'),
            endDay: new Date('2023-09-15'),
        },
        rating: 4.3,
        studentNumber: 200,
    },
    {
        id: 'course9',
        name: 'UI/UX Design Fundamentals',
        thumbnailUrl: 'https://via.placeholder.com/150',
        assignee: {
            id: 'assignee9',
            name: 'Grace Hall',
            avatarUrl: 'https://via.placeholder.com/150',
        },
        platform: 'Skillshare',
        period: {
            startDay: new Date('2023-01-10'),
            endDay: new Date('2023-07-10'),
        },
        rating: 4.7,
        studentNumber: 250,
    },
    {
        id: 'course10',
        name: 'Cloud Computing Basics',
        thumbnailUrl: 'https://via.placeholder.com/150',
        assignee: {
            id: 'assignee10',
            name: 'Henry Ford',
            avatarUrl: 'https://via.placeholder.com/150',
        },
        platform: 'Pluralsight',
        period: {
            startDay: new Date('2023-02-05'),
            endDay: new Date('2023-08-05'),
        },
        rating: 4.5,
        studentNumber: 175,
    },
    {
        id: 'course11',
        name: 'Ethical Hacking for Beginners',
        thumbnailUrl: 'https://via.placeholder.com/150',
        assignee: {
            id: 'assignee11',
            name: 'Ivy Jones',
            avatarUrl: 'https://via.placeholder.com/150',
        },
        platform: 'Cybrary',
        period: {
            startDay: new Date('2023-06-01'),
            endDay: new Date('2023-12-01'),
        },
        rating: 4.9,
        studentNumber: 300,
    },
    {
        id: 'course12',
        name: 'Project Management Professional (PMP)® Certification Prep',
        thumbnailUrl: 'https://via.placeholder.com/150',
        assignee: {
            id: 'assignee12',
            name: 'Jack Turner',
            avatarUrl: 'https://via.placeholder.com/150',
        },
        platform: 'PMI',
        period: {
            startDay: new Date('2023-04-20'),
            endDay: new Date('2023-10-20'),
        },
        rating: 4.6,
        studentNumber: 130,
    },
    {
        id: 'course13',
        name: 'Advanced JavaScript Concepts',
        thumbnailUrl: 'https://via.placeholder.com/150',
        assignee: {
            id: 'assignee13',
            name: 'Laura Mars',
            avatarUrl: 'https://via.placeholder.com/150',
        },
        platform: 'Udemy',
        period: {
            startDay: new Date('2023-05-01'),
            endDay: new Date('2023-11-01'),
        },
        rating: 4.7,
        studentNumber: 200,
    },
    {
        id: 'course14',
        name: 'Full Stack Web Development',
        thumbnailUrl: 'https://via.placeholder.com/150',
        assignee: {
            id: 'assignee14',
            name: 'Mike Johnson',
            avatarUrl: 'https://via.placeholder.com/150',
        },
        platform: 'Coursera',
        period: {
            startDay: new Date('2023-06-01'),
            endDay: new Date('2023-12-01'),
        },
        rating: 4.8,
        studentNumber: 220,
    },
    {
        id: 'course15',
        name: 'Introduction to Artificial Intelligence',
        thumbnailUrl: 'https://via.placeholder.com/150',
        assignee: {
            id: 'assignee15',
            name: 'Nina Brown',
            avatarUrl: 'https://via.placeholder.com/150',
        },
        platform: 'edX',
        period: {
            startDay: new Date('2023-07-01'),
            endDay: new Date('2024-01-01'),
        },
        rating: 4.5,
        studentNumber: 180,
    },
    {
        id: 'course16',
        name: 'Mobile App Development with Flutter',
        thumbnailUrl: 'https://via.placeholder.com/150',
        assignee: {
            id: 'assignee16',
            name: 'Oscar Davis',
            avatarUrl: 'https://via.placeholder.com/150',
        },
        platform: 'Udacity',
        period: {
            startDay: new Date('2023-08-01'),
            endDay: new Date('2024-02-01'),
        },
        rating: 4.6,
        studentNumber: 160,
    },
    {
        id: 'course17',
        name: 'Data Analysis with Python',
        thumbnailUrl: 'https://via.placeholder.com/150',
        assignee: {
            id: 'assignee17',
            name: 'Patricia Wilson',
            avatarUrl: 'https://via.placeholder.com/150',
        },
        platform: 'DataCamp',
        period: {
            startDay: new Date('2023-09-01'),
            endDay: new Date('2024-03-01'),
        },
        rating: 4.7,
        studentNumber: 140,
    },
    {
        id: 'course18',
        name: 'DevOps Essentials',
        thumbnailUrl: 'https://via.placeholder.com/150',
        assignee: {
            id: 'assignee18',
            name: 'Quincy Adams',
            avatarUrl: 'https://via.placeholder.com/150',
        },
        platform: 'LinkedIn Learning',
        period: {
            startDay: new Date('2023-10-01'),
            endDay: new Date('2024-04-01'),
        },
        rating: 4.8,
        studentNumber: 120,
    },
    {
        id: 'course19',
        name: 'Ethical Hacking: An Introduction',
        thumbnailUrl: 'https://via.placeholder.com/150',
        assignee: {
            id: 'assignee19',
            name: 'Rachel Green',
            avatarUrl: 'https://via.placeholder.com/150',
        },
        platform: 'Cybrary',
        period: {
            startDay: new Date('2023-11-01'),
            endDay: new Date('2024-05-01'),
        },
        rating: 4.9,
        studentNumber: 100,
    },
    {
        id: 'course20',
        name: 'Cloud Computing with AWS',
        thumbnailUrl: 'https://via.placeholder.com/150',
        assignee: {
            id: 'assignee20',
            name: 'Steven Hall',
            avatarUrl: 'https://via.placeholder.com/150',
        },
        platform: 'Amazon Web Services',
        period: {
            startDay: new Date('2023-12-01'),
            endDay: new Date('2024-06-01'),
        },
        rating: 4.6,
        studentNumber: 180,
    },
    {
        id: 'course21',
        name: 'Game Development with Unity',
        thumbnailUrl: 'https://via.placeholder.com/150',
        assignee: {
            id: 'assignee21',
            name: 'Tina Fey',
            avatarUrl: 'https://via.placeholder.com/150',
        },
        platform: 'Udemy',
        period: {
            startDay: new Date('2024-01-01'),
            endDay: new Date('2024-07-01'),
        },
        rating: 4.5,
        studentNumber: 150,
    },
    {
        id: 'course22',
        name: 'SEO Fundamentals',
        thumbnailUrl: 'https://via.placeholder.com/150',
        assignee: {
            id: 'assignee22',
            name: 'Umar Khan',
            avatarUrl: 'https://via.placeholder.com/150',
        },
        platform: 'Coursera',
        period: {
            startDay: new Date('2024-02-01'),
            endDay: new Date('2024-08-01'),
        },
        rating: 4.4,
        studentNumber: 130,
    }
    // Thêm nhiều khóa học mẫu khác nếu cần
];

export const fetchCourses = ():
    Promise<CourseType[]> => {
    // Mô phỏng việc gọi API bất đồng bộ và trả về dữ liệu sau 1 giây
    return new Promise((resolve) => {
        setTimeout(() => resolve(MockCourses), 0);
    });
};

export const fetchCourses2 = (offset: number, quantity: number):
    Promise<CourseType[]> => {
    return new Promise((resolve) => {
        setTimeout(() => {
            const result = MockCourses.slice(offset, offset + quantity);
            resolve(result);
        }, 3000);
    });
};