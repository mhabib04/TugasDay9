<?php

include 'connection.php';

if($_POST){

    //POST DATA
    $username = isset($_POST['username']) ? $_POST['username'] : '';
    $password = isset($_POST['password']) ? $_POST['password'] : '';
    $name = isset($_POST['name']) ? $_POST['name'] : '';

    $response = [];

    //Cek username didalam database
    $userQuery = $connection->prepare("SELECT * FROM user where username = ?");
    $userQuery->execute(array($username));

    // Cek apakah username sudah digunakan
    if($userQuery->rowCount() != 0){
        // Beri Response
        $response['status'] = false;
        $response['message'] = 'Akun "' . $username . '" sudah digunakan';
    } else {
        $insertAccount = 'INSERT INTO user (username, password, name) values (:username, :password, :name)';
        $statement = $connection->prepare($insertAccount);

        try {
            //Eksekusi statement db
            $statement->execute([
                ':username' => $username,
                ':password' => md5($password),
                ':name' => $name
            ]);

            //Beri response
            $response['status'] = true;
            $response['message'] = 'Akun "' . $username . '" berhasil didaftar';
            $response['data'] = [
                'username' => $username,
                'name' => $name
            ];
        } catch (Exception $e){
            die($e->getMessage());
        }

    }
    
    //Jadikan data JSON
    $json = json_encode($response, JSON_PRETTY_PRINT);

    //Print JSON
    echo $json;
}
?>
