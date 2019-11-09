<?php

class Feature
{
    private $id;
    private $title;
    private $teaser;
    private $text;
    private $hasBody;
    private static $features = [];

    private function __construct(string $id, string $title, string $teaser, string $text, bool $hasBody)
    {
        $this->id = strtolower(preg_replace('/[^a-zA-Z0-9]/', '', $id));
        $this->title = $title;
        $this->teaser = $teaser;
        $this->text = $text;
        $this->hasBody = $hasBody;
    }

    public static function getFeatures(string $categoryId) : array
    {
        if (empty(self::$features)) {
            $files = array_filter(glob("features/{$categoryId}/*.md"));
            foreach ($files as $file) {
                if (!file_exists($file)) continue;
                $pd = new Parsedown();
                $text = $pd->text(file_get_contents($file));
                $title = $teaser = "";
                $hasBody = true;

                $matches = [];
                if (preg_match('/<h1>(.*)<\/h1>/', $text, $matches)) {
                    $title = $matches[1];
                }
                if (preg_match_all('/<p>(.*)<\/p>/i', $text, $matches)) {
                    $teaser = $matches[1][0];
                    $hasBody = count($matches[1]) > 1;
                }

                $id = str_replace('.md', '', substr($file, strrpos($file, '/')));
                $feature = new Feature($id, $title, $teaser, $text, $hasBody);
                self::$features[$categoryId][] = $feature;
            }
        }

        return self::$features[$categoryId];
    }

    public function getId() : string
    {
        return $this->id;
    }

    public function getTitle() : string
    {
        return $this->title;
    }

    public function getTeaser() : string
    {
        return $this->teaser;
    }

    public function getText() : string
    {
        return $this->text;
    }

    public function getBody() : string
    {
        $body = $this->text;
        $body = preg_replace('/(<h1>.*<\/h1>)/', '', $body);
        return $body;
    }

    public function hasBody() : bool
    {
        return $this->hasBody;
    }
}