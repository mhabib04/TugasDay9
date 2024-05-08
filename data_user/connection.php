<?php

$connection = null;

try{
    //Config
    $host = "localhost";
    $username = "bibskymy_user";
    $password = "mhabib1804";
    $dbname = "bibskymy_data_user";

    //Connect
    $database = "mysql:dbname=$dbname;host=$host";
    $connection = new PDO($database, $username, $password);
    $connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    if($connection){
        echo "Koneksi Berhasil";
    } else {
        echo "Gagal gan";
    }


} catch (PDOException $e){
    echo "Error ! " . $e->getMessage();
    die;
}

?>