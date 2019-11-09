<?php 

if (!defined('LAUNCHED')) die(); 

$messages = [
    "I know what I'm doing",
    "Just download it already",
    "What are you waiting for?",
    "I think this is the right link",
    "Get it from CurseForge now!",
    "Download it from CurseForge now!",
    "Get the file, I'm done waiting",
    "I want the file now",
    "Stop annoying me and just download it"
];
$downloadMessage = $messages[mt_rand(0, count($messages)-1)];

?>

<div class="jumbotron jumbotron-fluid text-center charm-header">
    <div class="container">
        <h1 class="display-4"><span class="thin">Charm</span> Download</h1>
        <a class="btn btn-secondary" href="/"><span class="fa fa-home"></span> Back Home</a>
    </div>
</div>

<div class="container">

<div class="row mt-5 mb-5">
    <div class="col-12 text-center">
    <a class="btn btn-success" href="/download"><span class="fa fa-download"></span> <?php echo $downloadMessage; ?></a>
    </div>
</div>

<div class="row">
    <div class="col-12">
    <h2>Requirements</h2>
    <p>
        Charm is a <strong>Forge Mod</strong> for <strong>Minecraft 1.14.4</strong>.  You need to install the <strong>Java Edition</strong>
        of Minecraft and then the <a href="">latest version of Forge</a>.  Once you have done this, drop the <a href="">Charm file</a> in
        your <strong>mods</strong> folder and start the game.
    </p>
    <p>
        Charm does not require <strong>Quark</strong> to be installed but is designed to play nicely with it and extend some of its features.
        I cannot recommend Quark highly enough - it overhauls the vanilla experience of Minecraft in subtle and amazing ways.  <a href="">Download Quark now!</a>
    </p>
    <p>
        After the game starts for the first time, you can edit the <strong>charm-common.toml</strong> file (in the config folder) to change things about Charm.
        Every single module can be enabled or disabled and most modules allow you to tweak some of the finer settings to fit your playstyle.
    </p>
    </div>
</div>

</div>
