<?php

class Category
{
    private $id;
    private $name;
    private $description;
    private static $categories = [];

    private function __construct(string $id, array $data)
    {
        $this->id = strtolower(trim($id, '/'));
        $this->name = $data['name'];
        $this->description = $data['description'];
    }

    public static function getCategories() : array
    {
        if (empty(self::$categories)) {
            $json = file_get_contents("features/categories.json");
            $categories = json_decode($json, true);
            
            if ($err = json_last_error()) {
                throw new Exception("JSON parse error ".$err);
            }

            foreach ($categories as $index => $category) {
                self::$categories[$index] = new Category($index, $category);
            }
        }

        return self::$categories;
    }

    public function getId()
    {
        return $this->id;
    }

    public function getName()
    {
        return ucfirst($this->name);
    }

    public function getDescription()
    {
        return $this->description;
    }
}