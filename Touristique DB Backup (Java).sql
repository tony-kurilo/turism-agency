PGDMP                    
    |            Touristique DB (Java)    17.0    17.0                0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false                       0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false                       0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false                       1262    16492    Touristique DB (Java)    DATABASE     �   CREATE DATABASE "Touristique DB (Java)" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Russian_Ukraine.1251';
 '   DROP DATABASE "Touristique DB (Java)";
                     postgres    false            �            1259    16522    clients    TABLE     �   CREATE TABLE public.clients (
    id integer NOT NULL,
    name character varying(100),
    tel_number character varying(20),
    address character varying(255),
    agency_name character varying(100)
);
    DROP TABLE public.clients;
       public         heap r       postgres    false            �            1259    16537    hotel_images    TABLE     e   CREATE TABLE public.hotel_images (
    hotel character varying(100) NOT NULL,
    image_url jsonb
);
     DROP TABLE public.hotel_images;
       public         heap r       postgres    false            �            1259    16512    managers    TABLE     �   CREATE TABLE public.managers (
    id integer NOT NULL,
    name character varying(100),
    tel_number character varying(20),
    agency_name character varying(100)
);
    DROP TABLE public.managers;
       public         heap r       postgres    false            �            1259    16503    users    TABLE     F  CREATE TABLE public.users (
    id integer NOT NULL,
    username character varying(50) NOT NULL,
    password character varying(255) NOT NULL,
    role character varying(20) NOT NULL,
    CONSTRAINT users_role_check CHECK (((role)::text = ANY ((ARRAY['client'::character varying, 'manager'::character varying])::text[])))
);
    DROP TABLE public.users;
       public         heap r       postgres    false            �            1259    16502    users_id_seq    SEQUENCE     �   CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.users_id_seq;
       public               postgres    false    218                       0    0    users_id_seq    SEQUENCE OWNED BY     =   ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;
          public               postgres    false    217            �            1259    16544    voucher_requests    TABLE     1  CREATE TABLE public.voucher_requests (
    id integer NOT NULL,
    username character varying(100),
    country character varying(100),
    city character varying(100),
    hotel character varying(100),
    begin_date date,
    end_date date,
    state character varying(100),
    price numeric(10,2)
);
 $   DROP TABLE public.voucher_requests;
       public         heap r       postgres    false            �            1259    16532    vouchers    TABLE     �   CREATE TABLE public.vouchers (
    id integer NOT NULL,
    country character varying(100),
    city character varying(100),
    hotel character varying(100),
    begin_date date,
    end_date date
);
    DROP TABLE public.vouchers;
       public         heap r       postgres    false            k           2604    16506    users id    DEFAULT     d   ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);
 7   ALTER TABLE public.users ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    218    217    218                      0    16522    clients 
   TABLE DATA           M   COPY public.clients (id, name, tel_number, address, agency_name) FROM stdin;
    public               postgres    false    220   �!                 0    16537    hotel_images 
   TABLE DATA           8   COPY public.hotel_images (hotel, image_url) FROM stdin;
    public               postgres    false    222   2"                 0    16512    managers 
   TABLE DATA           E   COPY public.managers (id, name, tel_number, agency_name) FROM stdin;
    public               postgres    false    219   �"                 0    16503    users 
   TABLE DATA           =   COPY public.users (id, username, password, role) FROM stdin;
    public               postgres    false    218   �"                 0    16544    voucher_requests 
   TABLE DATA           r   COPY public.voucher_requests (id, username, country, city, hotel, begin_date, end_date, state, price) FROM stdin;
    public               postgres    false    223   >#                 0    16532    vouchers 
   TABLE DATA           R   COPY public.vouchers (id, country, city, hotel, begin_date, end_date) FROM stdin;
    public               postgres    false    221   �#                  0    0    users_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.users_id_seq', 1, false);
          public               postgres    false    217            t           2606    16526    clients clients_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.clients
    ADD CONSTRAINT clients_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.clients DROP CONSTRAINT clients_pkey;
       public                 postgres    false    220            x           2606    16543    hotel_images hotel_images_pkey 
   CONSTRAINT     _   ALTER TABLE ONLY public.hotel_images
    ADD CONSTRAINT hotel_images_pkey PRIMARY KEY (hotel);
 H   ALTER TABLE ONLY public.hotel_images DROP CONSTRAINT hotel_images_pkey;
       public                 postgres    false    222            r           2606    16516    managers managers_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.managers
    ADD CONSTRAINT managers_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.managers DROP CONSTRAINT managers_pkey;
       public                 postgres    false    219            n           2606    16509    users users_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public                 postgres    false    218            p           2606    16511    users users_username_key 
   CONSTRAINT     W   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);
 B   ALTER TABLE ONLY public.users DROP CONSTRAINT users_username_key;
       public                 postgres    false    218            z           2606    16548 &   voucher_requests voucher_requests_pkey 
   CONSTRAINT     d   ALTER TABLE ONLY public.voucher_requests
    ADD CONSTRAINT voucher_requests_pkey PRIMARY KEY (id);
 P   ALTER TABLE ONLY public.voucher_requests DROP CONSTRAINT voucher_requests_pkey;
       public                 postgres    false    223            v           2606    16536    vouchers vouchers_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.vouchers
    ADD CONSTRAINT vouchers_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.vouchers DROP CONSTRAINT vouchers_pkey;
       public                 postgres    false    221            |           2606    16527    clients clients_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.clients
    ADD CONSTRAINT clients_id_fkey FOREIGN KEY (id) REFERENCES public.users(id) ON DELETE CASCADE;
 A   ALTER TABLE ONLY public.clients DROP CONSTRAINT clients_id_fkey;
       public               postgres    false    4718    220    218            {           2606    16517    managers managers_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.managers
    ADD CONSTRAINT managers_id_fkey FOREIGN KEY (id) REFERENCES public.users(id) ON DELETE CASCADE;
 C   ALTER TABLE ONLY public.managers DROP CONSTRAINT managers_id_fkey;
       public               postgres    false    219    4718    218               W   1	Курило Антон Андрійович	+3806546213123	Гондор	Touristique
    3	das	123	sda	Touristique
    \.


         K   Hotel	["https://drive.google.com/uc?id=1hSR_5fm4PUrRGEdkuAnycN3DgK6MZ0pu"]
    \.


         4   2	Більбо Бєггінс	238013123	Touristique
    \.


            1	tony	qwe	client
    2	ivy	4ty	manager
    3	ads	123	client
    \.


         J   1	tony	France	Paris	Hotel	2024-11-19	2024-11-23	До сплати	1999.99
 J   2	tony	France	Paris	Hotel	2024-11-22	2024-11-23	До сплати	4999.99
    \.


         +   1	France	Paris	Hotel	2024-11-18	2024-12-29
    \.


     