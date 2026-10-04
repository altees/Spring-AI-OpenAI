package com.altees.OpenAIExample.config;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.altees.OpenAIExample.model.Employee;
import com.altees.OpenAIExample.repostiory.EmployeeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DataLoader {

    @Autowired
    private EmployeeRepository employeeRepository;

/*
    @PostConstruct
    public void loadDate() {
        loadEmployeeData();
    }*/

    /**
     * Load initial employee data at application startup
     */
    private void loadEmployeeData() {
        // Check if employees already exist
        if (employeeRepository.count() == 0) {
            List<Employee> employees = List.of(
                    new Employee(null, "Rajesh Kumar", "Senior Developer"),
                    new Employee(null, "Priya Sharma", "Project Manager"),
                    new Employee(null, "Amit Patel", "QA Engineer"),
                    new Employee(null, "Neha Singh", "UI/UX Designer"),
                    new Employee(null, "Vikram Desai", "DevOps Engineer"),
                    new Employee(null, "Anjali Verma", "Business Analyst"),
                    new Employee(null, "Rohit Joshi", "Junior Developer"),
                    new Employee(null, "Divya Nair", "Data Analyst")
            );
            employeeRepository.saveAll(employees);
            System.out.println("✓ Initial employee data loaded successfully!");
        }
    }

    /**
     * Load vector store data for RAG
     */
    public void loadVectorStoreData() {
        List<String> sentences = List.of(
                "Java is an object oriented programming language.",
                "Spring Boot simplifies Java application development.",
                "Microservices allow applications to be split into smaller services.",
                "REST APIs are commonly used for communication between systems.",
                "Artificial intelligence enables machines to perform human-like tasks.",
                "Machine learning is a subset of artificial intelligence.",
                "Neural networks are inspired by the human brain.",
                "Vector databases store embeddings for semantic search.",
                "Qdrant is a vector database designed for similarity search.",
                "Embeddings convert text into numerical vectors.",
                "PostgreSQL is a relational database system.",
                "MongoDB is a document-oriented NoSQL database.",
                "Redis is often used for caching data.",
                "Apache Kafka is used for event streaming.",
                "Docker helps package applications into containers.",
                "Linux is a widely used operating system.",
                "Cloud computing provides resources over the internet.",
                "Amazon Web Services offers many cloud services.",
                "Azure is Microsoft's cloud platform.",
                "Google Cloud Platform provides scalable infrastructure.",
                "India is one of the largest countries by population.",
                "New Delhi is the capital of India.",
                "Mumbai is known as the financial capital of India.",
                "Tokyo is the capital city of Japan.",
                "Paris is famous for the Eiffel Tower.",
                "Mount Everest is the highest mountain on Earth.",
                "The Pacific Ocean is the largest ocean.",
                "The Sun is a star at the center of our solar system.",
                "The Earth revolves around the Sun.",
                "The Moon revolves around the Earth.",
                "Water boils at one hundred degrees Celsius.",
                "Plants use photosynthesis to create energy.",
                "Trees release oxygen into the atmosphere.",
                "Elephants are among the largest land animals.",
                "Lions are known as kings of the jungle.",
                "Dolphins are intelligent marine animals.",
                "Birds use wings to fly.",
                "Electric cars use batteries instead of fuel.",
                "Solar panels convert sunlight into electricity.",
                "Wind turbines generate renewable energy.",
                "Python is popular for machine learning.",
                "JavaScript is used for web development.",
                "Angular is a frontend framework.",
                "React is a JavaScript library for UI development.",
                "Spring AI helps integrate AI into Java applications.",
                "Large language models generate human-like text.",
                "Chatbots can assist users automatically.",
                "RAG stands for Retrieval Augmented Generation.",
                "Semantic search focuses on meaning rather than keywords.",
                "Data structures organize information efficiently.",
                "Algorithms solve computational problems.",
                "Binary search is an efficient search algorithm.",
                "Sorting algorithms arrange data in order.",
                "Git helps developers manage source code.",
                "GitHub hosts software repositories.",
                "Continuous integration automates software testing.",
                "Continuous deployment automates software releases.",
                "JUnit is used for testing Java applications.",
                "Maven manages dependencies in Java projects.",
                "Gradle is another build automation tool.",
                "HTTP is a protocol for web communication.",
                "HTTPS adds security through encryption.",
                "TCP ensures reliable data transmission.",
                "UDP is faster but less reliable than TCP.",
                "RAM stores temporary computer data.",
                "SSDs are faster than traditional hard drives.",
                "Cybersecurity protects systems from attacks.",
                "Encryption secures sensitive information.",
                "Firewalls monitor network traffic.",
                "Virtual machines emulate computer systems.",
                "Artificial neural networks contain layers of nodes.",
                "Deep learning uses many neural network layers.",
                "OpenAI develops AI models and technologies.",
                "Natural language processing helps computers understand language.",
                "Speech recognition converts audio into text.",
                "Computer vision helps machines understand images.",
                "The Great Wall of China is a famous landmark.",
                "The Taj Mahal is located in India.",
                "The Nile is one of the longest rivers in the world.",
                "Sharks have existed for millions of years.",
                "Penguins cannot fly but can swim.",
                "Bananas contain potassium.",
                "Coffee contains caffeine.",
                "Exercise improves physical health.",
                "Walking regularly benefits the heart.",
                "Sleep is important for recovery.",
                "Reading books improves knowledge.",
                "Music can influence emotions.",
                "Chess is a strategic board game.",
                "Cricket is a popular sport in India.",
                "Football is played in many countries.",
                "The internet connects millions of devices.",
                "Smartphones have changed communication.",
                "Bluetooth enables wireless communication.",
                "WiFi connects devices to networks.",
                "Space exploration expands scientific understanding.",
                "Satellites orbit the Earth.",
                "Robots can automate repetitive tasks.",
                "Quantum computing is an emerging technology.",
                "The industrial revolution transformed manufacturing.",
                "The human body contains billions of cells.",
                "Education helps people develop skills.",
                "Time management increases productivity."
        );
        List<Document> docs = sentences.stream()
                .map(Document::new)
                .collect(Collectors.toList());

    }
}
