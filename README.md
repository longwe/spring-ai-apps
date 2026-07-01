# Spring AI Apps

Independent Spring Boot + Spring AI sample projects, organized by topic.

Each subdirectory is a standalone application — `cd` into it and run:

```bash
mvn spring-boot:run
```

## Structure

| Folder                                         | Topic                                         |
|------------------------------------------------|-----------------------------------------------|
| [01-chat/basic-chat](01-chat/basic-chat/)                | Chat with multi-model support and streaming   |
| [01-chat/chat-with-memory](01-chat/chat-with-memory/)    | Chat with per-session conversation memory     |
| [02-rag](02-rag/)                              | Retrieval-augmented generation                |
| [03-prompts](03-prompts/)                      | Prompt templates and engineering              |
| [04-structured-output](04-structured-output/) | JSON and typed model responses                |
| [05-agents](05-agents/)                        | Tool calling and agentic patterns             |
| [06-koog-agents](06-koog-agents/)              | AI agents using JetBrains Koog framework      |

## Root POM

The root `pom.xml` is a BOM-only parent (no `<modules>`). It pins the Spring Boot
and Spring AI versions so all projects stay in sync, but each project is built
and run independently — there is no Maven reactor linking them.

To add a new project, create a subdirectory with its own `pom.xml` that inherits
from the root:

```xml
<parent>
    <groupId>com.ezcloud</groupId>
    <artifactId>spring-ai-apps-rag</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <relativePath>../../pom.xml</relativePath>
</parent>
```
