POST	/api/v1/auth/login		Authenticate user and receive JWT token

GET		/api/v1/posts			Get all published posts
POST	/api/v1/posts			Create a new post
GET		/api/v1/posts/{id}		Get specific post
PUT		/api/v1/posts/{id}		Update existing post
DELETE	/api/v1/posts/({id}		Delete post
GET		/api/v1/posts/drafts	Get draft posts for authenticated user

GET		/api/v1/categories		Get all categories
POST	/api/v1/categories		Create category
DELETE	/api/v1/categories/{id}	Delete category

GET		/api/v1/tags			Get all tags
POST	/api/v1/tags			Create tags
DELETE	/api/v1/tags/{id}		Delete tag