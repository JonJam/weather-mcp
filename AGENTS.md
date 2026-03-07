# Agents and Project Guidance

This file defines how agents should work in this repository and where to find reusable skills.
- **Rules file**: Use this document to describe high-level behavior for agents working on this project.
- **Skills directory**: Custom skills live in the `.agents/skills/` directory.

## Rules

- **Purpose**: Describe project-specific expectations, conventions, and constraints for agents.
- **Examples**:
  - Coding style, frameworks, and libraries to prefer.
  - Files or areas of the codebase that need extra care.
  - Any constraints (e.g. no external network calls, no schema changes without approval).
### Spring Boot
Add rules below this line:


### Spring Boot
- **Dependency versions**: When using Spring Boot with the `io.spring.dependency-management` plugin, do not specify versions on individual dependencies in the `dependencies {}` block. Rely on BOMs (for example, `spring-boot-dependencies` and technology-specific BOMs like `spring-ai-bom`) to control versions centrally, as recommended in the Spring Boot dependency management documentation.

---

### Skills

- **Location**: `.agents/skills/`
- **Usage**: Each skill should be documented in its own file, explaining:
  - What the skill does.
  - When agents should use it.
  - Any configuration or assumptions.

You can start by adding markdown or JSON/YAML skill descriptions under `.agents/skills/`.
