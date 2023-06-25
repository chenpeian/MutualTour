--
-- PostgreSQL database dump
--

-- Dumped from database version 15.3 (Debian 15.3-1.pgdg110+1)
-- Dumped by pg_dump version 15.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE IF EXISTS mutual_tour;
--
-- Name: mutual_tour; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE mutual_tour WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.utf8';


ALTER DATABASE mutual_tour OWNER TO postgres;

\connect mutual_tour

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: blog; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.blog (
    uploader_id bigint NOT NULL,
    content text NOT NULL,
    type smallint NOT NULL,
    id uuid NOT NULL,
    tags character varying(10)[] NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    images text[] NOT NULL
);


ALTER TABLE public.blog OWNER TO postgres;

--
-- Name: comment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.comment (
    content text NOT NULL,
    commenter_id bigint NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    id uuid NOT NULL,
    blog_id uuid NOT NULL
);


ALTER TABLE public.comment OWNER TO postgres;

--
-- Name: t_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_user (
    id bigint NOT NULL,
    username character varying(20) NOT NULL,
    password character(60) NOT NULL,
    gender smallint,
    age smallint
);


ALTER TABLE public.t_user OWNER TO postgres;

--
-- Name: user_blog_liked_mapping; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_blog_liked_mapping (
    user_id bigint NOT NULL,
    blog_id uuid NOT NULL
);


ALTER TABLE public.user_blog_liked_mapping OWNER TO postgres;

--
-- Name: user_blog_starred_mapping; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_blog_starred_mapping (
    user_id bigint NOT NULL,
    blog_id uuid NOT NULL
);


ALTER TABLE public.user_blog_starred_mapping OWNER TO postgres;

--
-- Name: user_follower_mapping; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_follower_mapping (
    follower_id bigint NOT NULL,
    target_id bigint NOT NULL
);


ALTER TABLE public.user_follower_mapping OWNER TO postgres;

--
-- Name: user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.t_user ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Data for Name: blog; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.blog (uploader_id, content, type, id, tags, updated_at, images) FROM stdin;
1	test food	2	5fc277e1-03f7-4847-8db9-ee3d52424b71	{food,scene}	2023-06-13 23:28:00.746717	{}
1	123456789	2	89eca8cc-aad3-4643-9755-1ea9a6306b69	{}	2023-06-13 23:01:38.645949	{}
\.


--
-- Data for Name: comment; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.comment (content, commenter_id, created_at, id, blog_id) FROM stdin;
hhhhh啊啊啊啊	14	2023-06-14 01:39:30.3448	a09f3fbf-1047-485c-9050-97b89ea6ec72	5fc277e1-03f7-4847-8db9-ee3d52424b71
\.


--
-- Data for Name: t_user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.t_user (id, username, password, gender, age) FROM stdin;
14	test2	$2a$10$qLjPr3WJkhXaGoor0498dO4XXjY9JqgMtqDP4zWKQeR63iPxqVfpy	\N	\N
1	test1	$2a$10$p19vVHELBoAlDl/aEP695e5fWiCymVrNBQIeYIzOxAGb8IHrtzfpG	1	5
\.


--
-- Data for Name: user_blog_liked_mapping; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_blog_liked_mapping (user_id, blog_id) FROM stdin;
\.


--
-- Data for Name: user_blog_starred_mapping; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_blog_starred_mapping (user_id, blog_id) FROM stdin;
\.


--
-- Data for Name: user_follower_mapping; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_follower_mapping (follower_id, target_id) FROM stdin;
1	14
\.


--
-- Name: user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_id_seq', 14, true);


--
-- Name: blog blog_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.blog
    ADD CONSTRAINT blog_pk PRIMARY KEY (id);


--
-- Name: comment comment_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comment
    ADD CONSTRAINT comment_pk PRIMARY KEY (id);


--
-- Name: user_blog_liked_mapping user_blog_liked_mapping_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_blog_liked_mapping
    ADD CONSTRAINT user_blog_liked_mapping_pk PRIMARY KEY (user_id, blog_id);


--
-- Name: user_blog_starred_mapping user_blog_starred_mapping_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_blog_starred_mapping
    ADD CONSTRAINT user_blog_starred_mapping_pk PRIMARY KEY (user_id, blog_id);


--
-- Name: user_follower_mapping user_follower_mapping_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_follower_mapping
    ADD CONSTRAINT user_follower_mapping_pk PRIMARY KEY (follower_id, target_id);


--
-- Name: t_user user_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_user
    ADD CONSTRAINT user_pk PRIMARY KEY (id);


--
-- Name: t_user user_pk2; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_user
    ADD CONSTRAINT user_pk2 UNIQUE (username);


--
-- Name: blog_tags_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX blog_tags_index ON public.blog USING gin (tags);


--
-- Name: blog_type_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX blog_type_index ON public.blog USING btree (type);


--
-- Name: comment_content_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX comment_content_index ON public.comment USING btree (content);


--
-- Name: blog blog_user_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.blog
    ADD CONSTRAINT blog_user_id_fk FOREIGN KEY (uploader_id) REFERENCES public.t_user(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: comment comment_blog_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comment
    ADD CONSTRAINT comment_blog_id_fk FOREIGN KEY (blog_id) REFERENCES public.blog(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: comment comment_user_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comment
    ADD CONSTRAINT comment_user_id_fk FOREIGN KEY (commenter_id) REFERENCES public.t_user(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: user_blog_liked_mapping user_blog_liked_mapping_blog_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_blog_liked_mapping
    ADD CONSTRAINT user_blog_liked_mapping_blog_id_fk FOREIGN KEY (blog_id) REFERENCES public.blog(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: user_blog_liked_mapping user_blog_liked_mapping_user_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_blog_liked_mapping
    ADD CONSTRAINT user_blog_liked_mapping_user_id_fk FOREIGN KEY (user_id) REFERENCES public.t_user(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: user_blog_starred_mapping user_blog_starred_mapping_blog_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_blog_starred_mapping
    ADD CONSTRAINT user_blog_starred_mapping_blog_id_fk FOREIGN KEY (blog_id) REFERENCES public.blog(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: user_blog_starred_mapping user_blog_starred_mapping_user_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_blog_starred_mapping
    ADD CONSTRAINT user_blog_starred_mapping_user_id_fk FOREIGN KEY (user_id) REFERENCES public.t_user(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: user_follower_mapping user_follower_mapping_user_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_follower_mapping
    ADD CONSTRAINT user_follower_mapping_user_id_fk FOREIGN KEY (follower_id) REFERENCES public.t_user(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: user_follower_mapping user_follower_mapping_user_id_fk2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_follower_mapping
    ADD CONSTRAINT user_follower_mapping_user_id_fk2 FOREIGN KEY (target_id) REFERENCES public.t_user(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

